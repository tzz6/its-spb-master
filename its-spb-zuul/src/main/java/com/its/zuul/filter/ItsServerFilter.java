package com.its.zuul.filter;

import javax.servlet.http.HttpServletRequest;

import com.its.zuul.util.IpUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 网关过滤器
 * 
 * @author tzz
 */
public class ItsServerFilter extends ZuulFilter {

    /**
     * run：过滤器的具体逻辑
     */
    @Override
    public Object run() throws ZuulException {
		// 案例：拦截所有服务请求,判断服务接口上是否传递accessToken参数
		// 1.获取上下文
		RequestContext ctx = RequestContext.getCurrentContext();
		// 2.获取Request
		HttpServletRequest request = ctx.getRequest();

		// 3.从请求头中获取Token
		Object accessToken = request.getParameter("accessToken");
		// 请求的servletPath
		String servletPath = request.getServletPath();
		// String contextPath = request.getContextPath();// 项目请求路径
		String ip = IpUtil.getIpAddr(request);

		// 不需登录要拦截的url
		String[] notFilterUrl = new String[] { "v2/api-docs", "logout", "webcam", "test" };
		// IP白名单
		String[] ipFilter = new String[] { "127.0.0.1", "10.118.53.59", "10.118.14.16" };
		boolean flag = false;
		String message = null;
		if (!checkFilter(ip, ipFilter)) {
			if (checkFilter(servletPath, notFilterUrl)) {
				if (accessToken == null) {
					// flag = true;
					// message = "请求被Zuul网关拦截|accessToken is null";
				}
			}
		} else {
			flag = true;
			message = "请求被Zuul网关拦截|IP非法";
		}
		if (flag) {
			// 请求被拦截，不再调用服务接口，网关服务直接响应客户端
		    // 设置为false令zuul过滤该请求，不对其进行路由
			ctx.setSendZuulResponse(false);
			// 设置了其返回的错误码
			ctx.setResponseStatusCode(401);
			ctx.getResponse().setContentType("text/html;charset=UTF-8");
			// 设置了其返回的内容
			ctx.setResponseBody(message);
		}
		return null;
	}

	/**
	 * shouldFilter：返回一个boolean类型来判断该过滤器是否有效(过滤器的开关)true:有效/false:无效
	 */
    @Override
	public boolean shouldFilter() {
		return true;
	}

	/**
	 * filterOrder：通过int值来定义过滤器的执行顺序,当一个请求在同一阶存在多个过滤器的时候，设置执行顺序
	 */
	@Override
	public int filterOrder() {
		return 0;
	}

	/**
	 * filterType：该函数需要返回一个字符串来代表过滤器的类型，在Zuul中默认定义了四种不同生命周期的过滤器类型<br>
	 * pre：在请求被路由之前调用<br>
	 * routing：在请求路由时候被调用<br>
	 * post：在routing和error过滤器之后被调用<br>
	 * error：处理请求时发生错误时被调用 <br>
	 */
	@Override
	public String filterType() {
		return "pre";
	}

	/** 检查是否是要过滤的URL */
	protected boolean checkFilter(String url, String[] notFilterUrl) {
		for (String str : notFilterUrl) {
			if (url.indexOf(str) != -1) {
				return false;
			}
		}
		return true;
	}

}
