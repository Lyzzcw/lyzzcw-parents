package lyzzcw.work.component.xxljob.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: lzy
 * @version: 1.0
 * Date: 2023/3/8 10:53
 * Description: No Description
 */
@Slf4j
public class ApiJob extends IJobHandler {

    @Override
    public void execute() throws Exception {
        XxlJobHelper.log("XXL-JOB-API, Hello World.");

        log.info("my job api run");
    }
}
