package com.gofanx.annotation.param;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import com.gofanx.util.DBConf;
import com.gofanx.util.SomeStaticUtils;

@Path("tbkctx-resource")
public class TbkContextResource
{
	static String driver = "com.mysql.jdbc.Driver";
	static String url = "jdbc:mysql://localhost:3306/stock?autoReconnect=true&failOverReadOnly=false&maxReconnects=10&characterEncoding=utf8"; // 127.16.1.37
	static String user = "root";
	static String password = "root";
	static Connection connection;
	static Statement statement;
	static
	{
		try
		{
			InputStream is = TbkContextResource.class.getClassLoader().getResourceAsStream("application.properties");
			Properties prop = new Properties();
			try
			{
				prop.load(is);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			driver = prop.getProperty("jdbc.driver", driver);
			url = prop.getProperty("jdbc.url", url);
			user = prop.getProperty("jdbc.username", user);
			password = prop.getProperty("jdbc.password", password);
			connection = DBConf.getConnection(driver, url, user, password);
			statement = connection.createStatement();
			// connection.setAutoCommit(false);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	static String default_auctionid, default_actionid;
	static HashMap default_actionid_map;

	@GET
	@Path("/auction/default")
	@Produces(MediaType.TEXT_PLAIN)
	public String getTbkDefault(@Context final Application application, @Context final Request request, @Context final javax.ws.rs.ext.Providers provider,
			@Context final UriInfo uriInfo, @Context final HttpHeaders headers)
	{
		return "{\"ok\":true, \"data\": { \"itemurl\":\"http://detail.tmall.com/item.htm?id=" + default_auctionid + "\"}}";
	}

	@GET
	@Path("/notice/tbk")
	@Produces(MediaType.TEXT_PLAIN)
	public String getTbkNotice(@Context final Application application, @Context final Request request, @Context final javax.ws.rs.ext.Providers provider,
			@Context final UriInfo uriInfo, @Context final HttpHeaders headers)
	{
		return "{\"ok\":true, \"data\":\"公告：测试版上线啦\"}";
	}

	/**
	 * 提交提现申请
	 * 
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
		// System.out.print("addUserCash");
		// System.out.println(uriInfo.getRequestUri());

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
					return "{\"ok\":false}";
				}
			}
			else
			{
				return "{\"ok\":false}";
			}
		}
		else
		{
			return "{\"ok\":false}";
		}
		return "{\"ok\":true}";
	}

	/**
	 * 查询返现订单，和返现金额
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
		// System.out.print("getUserOrderList");
		// System.out.println(uriInfo.getRequestUri());

		final MultivaluedMap<String, String> headerMap = headers.getRequestHeaders();
		if (headerMap.containsKey("username") && headerMap.containsKey("gofanx-cookie"))
		{
			String username = headerMap.getFirst("username");
			String gofanxCookie = headerMap.getFirst("gofanx-cookie");
			if (gofanxCookie.contains("=" + username + ";"))
			{
				// select表user_order,taoke_report_detail,user_income
				// status:
				return "{\"ok\":true}";
			}
			else
			{
				return "{\"ok\":false}";
			}
		}
		else
		{
			return "{\"ok\":false}";
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
		// System.out.print("addUserOrder");
		// System.out.println(uriInfo.getRequestUri());

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
					return "{\"ok\":true}";
				}
				else
				{
					return "{\"ok\":false}";
				}
			}
			else
			{
				return "{\"ok\":false}";
			}
		}
		else
		{
			return "{\"ok\":false}";
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
		// System.out.print("getAuctionCode");
		// System.out.println(uriInfo.getRequestUri());

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

			return "{\"ok\":true, \"data\":{\"auctionid\":" + auctionid + ", \"auctionCodeUrl\":" + auctionCodeUrl + "}}";
		}
		else
		{
			return "{\"ok\":false}";
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
	 * @return pagelist
	 */
	@GET
	@Path("/auction/commissionRate")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAuctionCommissionRate(@Context final Application application, @Context final Request request,
			@Context final javax.ws.rs.ext.Providers provider, @Context final UriInfo uriInfo, @Context final HttpHeaders headers)
	{
		// System.out.print("getAuctionCommissionRate");
		// System.out.println(uriInfo.getRequestUri());

		String resultJson = "{\"ok\":false}";
		final MultivaluedMap<String, String> queryMap = uriInfo.getQueryParameters();

		if (queryMap.containsKey("auctionid"))
		{
			String auctionid = queryMap.getFirst("auctionid");
			// select表auction_list
			String actionid = null;

			String select = "select `actionid`,`status` from `gofanx`.`tbk_auction` where itemid=" + auctionid;

			try
			{
				ResultSet r = statement.executeQuery(select);
				if (r.next() && r.getInt("status") == 0)
				{
					if (r.getInt("status") == 0)
					{
						actionid = r.getString("actionid");
						resultJson = "{\"ok\":true, \"data\":{\"pagelist\":[" + actionid + "]}}";
					}
					else if (r.getInt("status") == 1)
					{
						resultJson = "{\"ok\":true, \"data\":{\"pagelist\":[" + actionid + "]}}";
					}
				}
				else
				{
					resultJson = "{\"ok\":false}";
				}
				r.close();
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return resultJson;
		}
		else
		{
			return "{\"ok\":false}";
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
		// System.out.print("addAuctionCommission");
		// System.out.println(DecodeURL.decode(uriInfo.getRequestUri().toString()));

		final MultivaluedMap<String, String> queryMap = uriInfo.getQueryParameters();

		// if (queryMap.containsKey("auctionid") &&
		// queryMap.containsKey("actionid"))
		if (queryMap.containsKey("auctionid"))
		{
			String auctionid = queryMap.getFirst("auctionid");
			String actionid = queryMap.getFirst("actionid");
			String t = SomeStaticUtils.DATEFORMAT1.format(Long.parseLong(queryMap.getFirst("t")));

			if (actionid == null)
			{
				// 此页面无返现
				// insert update表auction_list
				String insert = "INSERT INTO `gofanx`.`tbk_auction` " + "(`itemid`, `created`, `status`)" + "VALUES " + "(" + auctionid + ",'" + t + "',1)"
						+ "ON DUPLICATE KEY UPDATE " + "`status`=VALUES(`status`)";
				try
				{
					statement.execute(insert);
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					// HashMap actionid =
					// SomeStaticUtils.gson.fromJson(queryMap.getFirst("actionid"),
					// HashMap.class);
					HashMap actionidMap = (HashMap) SomeStaticUtils.yaml.load(actionid);

					try
					{
						double zkPrice = Double.parseDouble(actionidMap.get("zkPrice").toString());
						double commissionRatePercent = Double.parseDouble(actionidMap.get("commissionRatePercent").toString());
						if (default_actionid_map == null || zkPrice > 1000 && commissionRatePercent >= 45)
						{
							default_auctionid = actionidMap.get("auctionId").toString();
							default_actionid = actionid;
							default_actionid_map = new HashMap(actionidMap);
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					// insert update表auction_list
					String insert = "INSERT INTO `gofanx`.`tbk_auction` " + "(`itemid`, `actionid`, `alikeid`, `created`, `updated`, `status`)" + "VALUES "
							+ "(" + actionidMap.get("auctionId").toString() + ",'" + actionid + "'," + auctionid + ",'" + t + "','" + t + "',0)"
							+ "ON DUPLICATE KEY UPDATE " + "`actionid`=VALUES(`actionid`), `alikeid`=VALUES(`alikeid`), `status`=VALUES(`status`)";

					try
					{
						statement.execute(insert);
					}
					catch (SQLException e)
					{
						e.printStackTrace();
					}
				}
				catch (Exception e)
				{
				}
			}

			String resultJson = "{\"ok\":true, \"data\":{\"pagelist\":[" + actionid + "]}}";

			return resultJson;
		}
		else
		{
			return "{\"ok\":false}";
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
