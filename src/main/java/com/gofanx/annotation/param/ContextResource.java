package com.gofanx.annotation.param;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Path("ctx-resource")
public class ContextResource
{
	/**
	 * @param application
	 * @param request
	 * @param provider
	 * @param uriInfo
	 * @param headers
	 * @return
	 */
	@GET
	@Path("/user/cash")
	@Produces(MediaType.TEXT_PLAIN)
	public String addUserCash(@Context final Application application, @Context final Request request, @Context final javax.ws.rs.ext.Providers provider,
			@Context final UriInfo uriInfo, @Context final HttpHeaders headers)
	{
//		System.out.print("addUserCash");
		System.out.println(uriInfo.getRequestUri());

		final MultivaluedMap<String, String> queryMap = uriInfo.getQueryParameters();

		if (queryMap.containsKey("alipayAccount"))
		{
			String alipayAccount = queryMap.getFirst("alipayAccount");
			String phone = queryMap.getFirst("phone");

			final MultivaluedMap<String, String> headerMap = headers.getRequestHeaders();
			if (headerMap.containsKey("username") && headerMap.containsKey("gofanx-cookie"))
			{
				String username = headerMap.getFirst("username");
				String gofanxCookie = headerMap.getFirst("gofanx-cookie");
				if (gofanxCookie.contains("=" + username + ";"))
				{
					// insert update表user_cash
				}
				else
				{
					return "{ok:false}";
				}
			}
			else
			{
				return "{ok:false}";
			}
		}
		else
		{
			return "{ok:false}";
		}
		return "{ok:true}";
	}

	/**
	 * 
	 * @param application
	 * @param request
	 * @param provider
	 * @param uriInfo
	 * @param headers
	 * @return
	 */
	@GET
	@Path("/user/orderList")
	@Produces(MediaType.TEXT_PLAIN)
	public String getUserOrderList(@Context final Application application, @Context final Request request, @Context final javax.ws.rs.ext.Providers provider,
			@Context final UriInfo uriInfo, @Context final HttpHeaders headers)
	{
//		System.out.print("getUserOrderList");
		System.out.println(uriInfo.getRequestUri());

		final MultivaluedMap<String, String> headerMap = headers.getRequestHeaders();
		if (headerMap.containsKey("username") && headerMap.containsKey("gofanx-cookie"))
		{
			String username = headerMap.getFirst("username");
			String gofanxCookie = headerMap.getFirst("gofanx-cookie");
			if (gofanxCookie.contains("=" + username + ";"))
			{
				// select表user_order,taoke_report_detail,user_income
				// status:
				return "{ok:true}";
			}
			else
			{
				return "{ok:false}";
			}
		}
		else
		{
			return "{ok:false}";
		}
	}

	/**
	 * 登记订单
	 * 
	 * @param application
	 * @param request
	 * @param provider
	 * @param uriInfo
	 * @param headers
	 * @return
	 */
	@GET
	@Path("/user/order")
	@Produces(MediaType.TEXT_PLAIN)
	public String addUserOrder(@Context final Application application, @Context final Request request, @Context final javax.ws.rs.ext.Providers provider,
			@Context final UriInfo uriInfo, @Context final HttpHeaders headers)
	{
//		System.out.print("addUserOrder");
		System.out.println(uriInfo.getRequestUri());

		final MultivaluedMap<String, String> queryMap = uriInfo.getQueryParameters();

		if (queryMap.containsKey("orderid"))
		{
			String orderid = queryMap.getFirst("orderid");

			final MultivaluedMap<String, String> headerMap = headers.getRequestHeaders();
			if (headerMap.containsKey("username") && headerMap.containsKey("gofanx-cookie"))
			{
				String username = headerMap.getFirst("username");
				String gofanxCookie = headerMap.getFirst("gofanx-cookie");
				if (gofanxCookie.contains("=" + username + ";"))
				{
					// insert update表user_order
					return "{ok:true}";
				}
				else
				{
					return "{ok:false}";
				}
			}
			else
			{
				return "{ok:false}";
			}
		}
		else
		{
			return "{ok:false}";
		}
	}

	/**
	 * 获取商品推广链接
	 * 
	 * @param application
	 * @param request
	 * @param provider
	 * @param uriInfo
	 * @param headers
	 * @return
	 */
	@GET
	@Path("/code/getAuctionCode")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAuctionCode(@Context final Application application, @Context final Request request, @Context final javax.ws.rs.ext.Providers provider,
			@Context final UriInfo uriInfo, @Context final HttpHeaders headers)
	{
//		System.out.print("getAuctionCode");
		System.out.println(uriInfo.getRequestUri());

		final MultivaluedMap<String, String> queryMap = uriInfo.getQueryParameters();

		if (queryMap.containsKey("auctionid"))
		{
			String auctionid = queryMap.getFirst("auctionid");
			// select表ali_trackid
			String ali_trackid = "2:mm_42209920_12002433_42798403";
			long t = System.currentTimeMillis() / 1000;
			if (queryMap.containsKey("t"))
			{
				ali_trackid += ":" + queryMap.getFirst("t") + "_0_1";
			}
			else
			{
				ali_trackid += ":" + t + "_0_0";
			}
			String auctionCodeUrl = "https://item.taobao.com/item.htm?id=" + auctionid + "&ali_trackid=" + ali_trackid;
			if (queryMap.containsKey("url"))
			{
				String url = queryMap.getFirst("url");
				if (url.indexOf("ali_trackid=") > -1)
				{
					String url2 = url.split("ali_trackid=", 2)[1];
					if (url2.indexOf("&") > -1)
					{
						url2 = url.split("&")[0];
					}
					auctionCodeUrl = url.replace(url2, ali_trackid);
				}
				else
				{
					auctionCodeUrl = url + "&" + ali_trackid;
				}
			}

			return "{ok:true, data:{auctionid:auctionid, auctionCodeUrl:" + auctionCodeUrl + "}}";
		}
		else
		{
			return "{ok:false}";
		}
	}

	/**
	 * 获取推广宝贝id的佣金和佣金率
	 * 
	 * @param application
	 * @param request
	 * @param provider
	 * @param uriInfo
	 * @param headers
	 * @return
	 */
	@GET
	@Path("/auction/commissionRate")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAuctionCommissionRate(@Context final Application application, @Context final Request request,
			@Context final javax.ws.rs.ext.Providers provider, @Context final UriInfo uriInfo, @Context final HttpHeaders headers)
	{
//		System.out.print("getAuctionCommissionRate");
		System.out.println(uriInfo.getRequestUri());

		final MultivaluedMap<String, String> queryMap = uriInfo.getQueryParameters();

		if (queryMap.containsKey("auctionid"))
		{
			String auctionid = queryMap.getFirst("auctionid");
			// select表auction_list
			double calCommission = 0;
			double commissionRatePercent = 0;

			return "{ok:true, data:{auctionid:" + auctionid + ", calCommission:" + calCommission + ", commissionRatePercent:" + commissionRatePercent + "}}";
		}
		else
		{
			return "{ok:false}";
		}
	}

	/**
	 * 添加推广宝贝id，佣金和佣金率
	 * 
	 * @param application
	 * @param request
	 * @param provider
	 * @param uriInfo
	 * @param headers
	 * @return
	 */
	@GET
	@Path("/auction/commission")
	@Produces(MediaType.TEXT_PLAIN)
	public String addAuctionCommission(@Context final Application application, @Context final Request request,
			@Context final javax.ws.rs.ext.Providers provider, @Context final UriInfo uriInfo, @Context final HttpHeaders headers)
	{
//		System.out.print("addAuctionCommission");
		System.out.println(uriInfo.getRequestUri());
		final StringBuilder buf = new StringBuilder();

		final MultivaluedMap<String, String> queryMap = uriInfo.getQueryParameters();
		buf.append("QUERY_PARAMETERS:\n");
		iterating(buf, queryMap);

		if (queryMap.containsKey("auctionid") && queryMap.containsKey("calCommission") && queryMap.containsKey("commissionRatePercent"))
		{
			String auctionid = queryMap.getFirst("auctionid");
			String calCommission = queryMap.getFirst("calCommission");
			String commissionRatePercent = queryMap.getFirst("commissionRatePercent");
			String t = queryMap.getFirst("t");
			// insert update表auction_list

			return "{ok:true, data:{" + buf.toString() + "}}";
		}
		else
		{
			return "{ok:false}";
		}
	}

	@GET
	@Path("{region:.+}/shenyang/{district:\\w+}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getByAddress(@Context final Application application, @Context final Request request, @Context final javax.ws.rs.ext.Providers provider,
			@Context final UriInfo uriInfo, @Context final HttpHeaders headers)
	{
		System.out.println(uriInfo.getRequestUri());
		final StringBuilder buf = new StringBuilder();
		final String path = uriInfo.getPath();
		buf.append("PATH=").append(path).append("\n");

		final MultivaluedMap<String, String> pathMap = uriInfo.getPathParameters();
		buf.append("PATH_PARAMETERS:\n");
		iterating(buf, pathMap);

		final MultivaluedMap<String, String> queryMap = uriInfo.getQueryParameters();
		buf.append("QUERY_PARAMETERS:\n");
		iterating(buf, queryMap);

		final List<PathSegment> segmentList = uriInfo.getPathSegments();
		buf.append("PATH_SEGMENTS:\n");
		for (final PathSegment pathSegment : segmentList)
		{
			final MultivaluedMap<String, String> matrix = pathSegment.getMatrixParameters();
			final String segmentPath = pathSegment.getPath();
			buf.append(matrix);
			buf.append(segmentPath);
		}
		buf.append("\nHEAD:\n");
		final MultivaluedMap<String, String> headerMap = headers.getRequestHeaders();
		iterating(buf, headerMap);
		buf.append("COOKIE:\n");
		final Map<String, Cookie> kukyMap = headers.getCookies();
		final Iterator<Entry<String, Cookie>> i = kukyMap.entrySet().iterator();
		while (i.hasNext())
		{
			final Entry<String, Cookie> e = i.next();
			final String k = e.getKey();
			buf.append("key=").append(k).append(",value=");
			final Cookie cookie = e.getValue();
			buf.append(cookie.getValue()).append(" ");
			buf.append("\n");
		}
		return buf.toString();
	}

	private void iterating(final StringBuilder buf, final MultivaluedMap<String, String> pathMap)
	{
		final Iterator<Entry<String, List<String>>> i = pathMap.entrySet().iterator();
		while (i.hasNext())
		{
			final Entry<String, List<String>> e = i.next();
			final String k = e.getKey();
			buf.append("key=").append(k).append(",value=");
			final List<String> vList = e.getValue();
			for (final String v : vList)
			{
				buf.append(v).append(" ");
			}
			buf.append("\n");
		}
	}
}
