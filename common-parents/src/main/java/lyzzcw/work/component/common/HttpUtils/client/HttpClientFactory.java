package lyzzcw.work.component.common.HttpUtils.client;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/08/5
 * Time: 15:52
 * Description: No Description
 */
public class HttpClientFactory {

	private static HttpClientFactory instance = null;

	private HttpClientFactory() {
	}

	public synchronized static HttpClientFactory getInstance() {
		if (instance == null) {
			instance = new HttpClientFactory();
		}
		return instance;
	}

	public synchronized HttpClient getHttpClient() {
		HttpClient httpClient = null;
		if (HttpConstant.IS_KEEP_ALIVE) {
			// 获取长连接
			httpClient = new KeepAliveHttpClientBuilder().getKeepAliveHttpClient();
		} else {
			// 获取短连接
			httpClient = new HttpClientBuilder().getHttpClient();
		}
		return httpClient;
	}

	public synchronized  CloseableHttpClient  getHttpsClient() {
		return createSSLClientDefault();
	}
	public HttpPost httpPost(String httpUrl) {
		HttpPost httpPost = null;
		httpPost = new HttpPost(httpUrl);
		if (HttpConstant.IS_KEEP_ALIVE) {
			// 设置为长连接，服务端判断有此参数就不关闭连接。
			httpPost.setHeader("Connection", "Keep-Alive");
		}
		return httpPost;
	}

	private static class KeepAliveHttpClientBuilder {

		private static HttpClient httpClient;

		/**
		 * 获取http长连接
		 */
		private synchronized HttpClient getKeepAliveHttpClient() {
			if (httpClient == null) {
				LayeredConnectionSocketFactory sslsf = null;
				try {
					sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}

				Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
						.<ConnectionSocketFactory>create().register("https", sslsf)
						.register("http", new PlainConnectionSocketFactory()).build();
				PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
				cm.setMaxTotal(HttpConstant.MAX_TOTAL);
				cm.setDefaultMaxPerRoute(HttpConstant.MAX_CONN_PER_ROUTE);

				RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HttpConstant.CONNECT_TIMEOUT)
						.setSocketTimeout(HttpConstant.SOCKET_TIMEOUT).build();
				// 创建连接
				httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(cm)
						.build();
			}

			return httpClient;
		}
	}

	private static class HttpClientBuilder {
		private HttpClient httpClient;

		/**
		 * 获取http短连接
		 */
		private synchronized HttpClient getHttpClient() {
			if (httpClient == null) {
				RequestConfig requestConfig = RequestConfig.custom()
						// 设置请求超时时间
						.setConnectTimeout(HttpConstant.CONNECT_TIMEOUT)
						// 设置响应超时时间
						.setSocketTimeout(HttpConstant.SOCKET_TIMEOUT).build();
				// 创建连接
				httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
			}
			return httpClient;
		}
	}

	public static CloseableHttpClient createSSLClientDefault() {
		try {
			// 使用 loadTrustMaterial() 方法实现一个信任策略，信任所有证书
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			// NoopHostnameVerifier类: 作为主机名验证工具，实质上关闭了主机名验证，它接受任何
			// 有效的SSL会话并匹配到目标主机。
			HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return HttpClients.createDefault();
	}

}