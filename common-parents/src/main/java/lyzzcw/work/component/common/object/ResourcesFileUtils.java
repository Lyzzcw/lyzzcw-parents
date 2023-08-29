package lyzzcw.work.component.common.object;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.net.URLDecoder;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/8/1
 * Time: 9:50
 * Description: 读取resources目录下的文件路径（九种方式）
 */
public class ResourcesFileUtils {
    /**
     * 根据文件路径读取文件内容
     *
     * @param fileInPath
     * @throws IOException
     */
    public static void getFileContent(Object fileInPath) throws IOException {
        BufferedReader br = null;
        if (fileInPath == null) {
            return;
        }
        if (fileInPath instanceof String) {
            br = new BufferedReader(new FileReader(new File((String) fileInPath)));
        } else if (fileInPath instanceof InputStream) {
            br = new BufferedReader(new InputStreamReader((InputStream) fileInPath));
        }
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    }

    /**
     * 方式一  主要核心方法是使用getResource和getPath方法，这里的getResource("")里面是空字符串
     * @param fileName
     * @throws IOException
     */
    public void function1(String fileName) throws IOException {
        String path = this.getClass().getClassLoader().getResource("").getPath();//注意getResource("")里面是空字符串
        System.out.println(path);
        String filePath = path + fileName;
        System.out.println(filePath);
        getFileContent(filePath);
    }

    /**
     * 方式二 直接通过文件名getPath来获取路径
     *
     * @param fileName
     * @throws IOException
     */
    public void function2(String fileName) throws IOException {
        String path = this.getClass().getClassLoader().getResource(fileName).getPath();//注意getResource("")里面是空字符串
        System.out.println(path);
        String filePath = URLDecoder.decode(path, "UTF-8");//如果路径中带有中文会被URLEncoder,因此这里需要解码
        System.out.println(filePath);
        getFileContent(filePath);
    }

    /**
     * 方式三 直接通过文件名+getFile()来获取
     *
     * @param fileName
     * @throws IOException
     */
    public void function3(String fileName) throws IOException {
        String path = this.getClass().getClassLoader().getResource(fileName).getFile();//注意getResource("")里面是空字符串
        System.out.println(path);
        String filePath = URLDecoder.decode(path, "UTF-8");//如果路径中带有中文会被URLEncoder,因此这里需要解码
        System.out.println(filePath);
        getFileContent(filePath);
    }

    /**
     * 方式四（重要）
     * 直接使用getResourceAsStream方法获取流
     * springboot项目中需要使用此种方法，因为jar包中没有一个实际的路径存放文件
     *
     * @param fileName
     * @throws IOException
     */
    public void function4(String fileName) throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
        getFileContent(in);
    }

    /**
     *
     * 方式五（重要）
     * 直接使用getResourceAsStream方法获取流
     * 如果不使用getClassLoader，可以使用getResourceAsStream("/配置测试.txt")直接从resources根路径下获取
     *
     * @param fileName
     * @throws IOException
     */
    public void function5(String fileName) throws IOException {
        InputStream in = this.getClass().getResourceAsStream("/" + fileName);
        getFileContent(in);
    }

    /**
     * 方式六（重要）
     * 通过ClassPathResource类获取，建议SpringBoot中使用
     * springboot项目中需要使用此种方法，因为jar包中没有一个实际的路径存放文件
     *
     * @param fileName
     * @throws IOException
     */
    public void function6(String fileName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(fileName);
        InputStream inputStream = classPathResource.getInputStream();
        getFileContent(inputStream);
    }

    /**
     * 方式七 通过绝对路径获取项目中文件的位置（不能用于服务器）
     * @param fileName
     * @throws IOException
     */
    public void function7(String fileName) throws IOException {
        String rootPath = System.getProperty("user.dir");//E:\WorkSpace\Git\spring-framework-learning-example
        String filePath = rootPath + "\\chapter-2-springmvc-quickstart\\src\\main\\resources\\"+fileName;
        getFileContent(filePath);
    }

    /**
     * 方式八  通过绝对路径获取项目中文件的位置（不能用于服务器）
     * @param fileName
     * @throws IOException
     */
    public void function8(String fileName) throws IOException {
        //参数为空
        File directory = new File("");
        //规范路径：getCanonicalPath() 方法返回绝对路径，会把 ..\ 、.\ 这样的符号解析掉
        String rootCanonicalPath = directory.getCanonicalPath();
        //绝对路径：getAbsolutePath() 方法返回文件的绝对路径，如果构造的时候是全路径就直接返回全路径，如果构造时是相对路径，就返回当前目录的路径 + 构造 File 对象时的路径
        String rootAbsolutePath =directory.getAbsolutePath();
        System.out.println(rootCanonicalPath);
        System.out.println(rootAbsolutePath);
        String filePath = rootCanonicalPath + "\\chapter-2-springmvc-quickstart\\src\\main\\resources\\"+fileName;
        getFileContent(filePath);
    }

    /**
     * 方式九 主要是通过设置环境变量，将文件放在环境变量中，原理也是通过绝对路径获取。
     * 通过绝对路径获取项目中文件的位置
     *
     * @param fileName
     * @throws IOException
     */
    public void function9(String fileName) throws IOException {
        System.setProperty("TEST_ROOT","E:\\WorkSpace\\Git\\spring-framework-learning-example");
        //参数为空
        String rootPath = System.getProperty("TEST_ROOT");
        System.out.println(rootPath);
        String filePath = rootPath + "\\chapter-2-springmvc-quickstart\\src\\main\\resources\\"+fileName;
        getFileContent(filePath);
    }
}
