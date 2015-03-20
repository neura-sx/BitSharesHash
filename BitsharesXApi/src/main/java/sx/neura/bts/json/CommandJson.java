package sx.neura.bts.json;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonWriter;

import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;

public abstract class CommandJson {

	public static final boolean LIVE_MODE = true;
	
	public static final boolean USE_CLIENT_STARTER = true;
	public static final String HOST = "localhost?";
	public static final int PORT = 9989;
	
	private static final String RPC_PATH = "/rpc";
	private static final String USER_NAME = "test";
	private static final String PASSWORD = "test";
	private static final String JSONRPC = "2.0";

	private static CloseableHttpClient client;
	
	private JsonStructure js;
	private String s;
	private String content;
	

	//public static void main(String[] args) {
		//doTest("about");
		//doTest("get_info");
		//doTest("wallet_lock");
		//doTest("wallet_unlock", new Object[] { 10000, "southpark" });
		//doTest("blockchain_list_assets", new Object[] { "", -1 });
	//}

	protected CommandJson doRun(int id, String command) throws BTSSystemException {
		return doRun(id, command, null, null);
	}

	protected CommandJson doRun(int id, String command, Object[] params) throws BTSSystemException {
		return doRun(id, command, params, null);
	}

	protected CommandJson doRun(int id, String command, Object[] params, Filter filter) throws BTSSystemException {
		
		String content = (LIVE_MODE ? getJsonContent(id, command, getJab(params)) : hack(params));
		if (content == null) {
			Error error = new Error();
			error.setMessage("API Connection Error");
			throw new BTSSystemException(error);
		}
		if (filter != null)
			content = filter.filter(content);
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
				.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
				//.configure(DeserializationConfig.Feature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true)
				.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		CommandJson cj = null;
		try {
			cj = mapper.readValue(content, getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
		cj.content = content;
		return cj;
	}
	
	protected static CommandJson doTest(String command) {
		return doTest(command, null);
	}
	
	protected static CommandJson doTest(String command, Object[] params) {
		CommandJson cj = new CommandJson() {
			@Override
			protected String hack(Object[] params) {
				return null;
			}
		};
		cj.js = null;
		try {
			cj.content = getJsonContent(0, command, getJab(params));
			if (cj.content == null)
				return null;
			JsonReader jr = Json.createReader(new StringReader(cj.content));
			JsonObject jo = (JsonObject) jr.read();
			JsonValue err = jo.get("error");
			if (err != null) {
				cj.js = jo.getJsonObject("error");
			} else {
				JsonValue res = jo.get("result");
				if (res.toString().equals("null")) {
					System.out.println(String.format("result: %s", res.toString()));
				} else if (res instanceof JsonNumber || res instanceof JsonString) {
					System.out.println(String.format("result: %s", res.toString()));
				} else {
					try {
						cj.js = jo.getJsonObject("result");
					} catch (Exception e) {
						cj.js = jo.getJsonArray("result");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println(e.getMessage());
			return null;
		}
		System.out.println(cj.getContent());
		System.out.println();
		System.out.println(cj.getTree());
		System.out.println(cj.getDefinition());
		return cj;
	}
	
	private static JsonArrayBuilder getJab(Object[] params) {
		if (params == null)
			params = new Object[0];
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for (Object obj : params) {
			if (obj.getClass().equals(String.class))
				jab = jab.add((String) obj);
			else if (obj.getClass().equals(Integer.class))
				jab = jab.add((Integer) obj);
			else if (obj.getClass().equals(Double.class))
				jab = jab.add((Double) obj);
			else if (obj.getClass().equals(Long.class))
				jab = jab.add((Long) obj);
			else if (obj.getClass().equals(Boolean.class))
				jab = jab.add((Boolean) obj);
			else if (obj.getClass().equals(BigDecimal.class))
				jab = jab.add((BigDecimal) obj);
			else if (obj.getClass().equals(BigInteger.class))
				jab = jab.add((BigInteger) obj);
		}
		return jab;
	}

	private static void commandHeaders(HttpPost post) {
		post.addHeader(new BasicHeader("User-Agent",
				CommandJson.class.getSimpleName()));
		try {
			post.addHeader(new BasicScheme().authenticate(
					new UsernamePasswordCredentials(USER_NAME, PASSWORD), post,
					null));
		} catch (org.apache.http.auth.AuthenticationException e) {
			e.printStackTrace();
		}
		post.addHeader(new BasicHeader("Host", "localhost:9989"));
		post.addHeader(new BasicHeader("Connection", "keep-alive"));
		post.addHeader(new BasicHeader("Accept",
				"application/json, text/plain, */*"));
		post.addHeader(new BasicHeader("Origin", "http://localhost:9989"));
		post.addHeader(new BasicHeader("Content-Type",
				"application/json;charset=UTF-8"));
		post.addHeader(new BasicHeader("Referer", "http://localhost:9989/"));
		post.addHeader(new BasicHeader("Accept-Encoding", "gzip,deflate"));
		post.addHeader(new BasicHeader("Accept-Language",
				"en-US,en;q=0.8,pl;q=0.6"));
	}

	private static JsonObject getCommandEntity(int id, String command,
			JsonArrayBuilder params) {
		JsonObject jo = Json.createObjectBuilder().add("jsonrpc", JSONRPC)
				.add("id", id).add("method", command).add("params", params)
				.build();
		return jo;
	}

	private static String getJsonContent(int id, String command, JsonArrayBuilder params) {
		if (client == null)
			client = HttpClientBuilder.create().build();
		String retval = "";
		try {
			HttpPost post = new HttpPost(new URI("http", null, HOST, PORT,
					RPC_PATH, null, null));
			commandHeaders(post);
			StringWriter sw = new StringWriter();
			{
				JsonWriter jw = Json.createWriter(sw);
				jw.writeObject(getCommandEntity(id, command, params));
				jw.close();
			}
			post.setEntity(new StringEntity(sw.toString()));

			HttpResponse response = client.execute(post);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				retval += inputLine;
			in.close();
		} catch (Exception e) {
			//e.printStackTrace();
			System.err.println(e.getMessage());
			return null;
		}
		return retval;
	}

	private void navigateTree(JsonValue tree, String key) {
		if (key != null)
			s += key + ": ";
		switch (tree.getValueType()) {
		case OBJECT:
			s += "OBJECT\n";
			JsonObject object = (JsonObject) tree;
			for (String name : object.keySet())
				navigateTree(object.get(name), name);
			break;
		case ARRAY:
			s += "ARRAY\n";
			JsonArray array = (JsonArray) tree;
			for (JsonValue val : array)
				navigateTree(val, null);
			break;
		case STRING:
			JsonString st = (JsonString) tree;
			s += "STRING " + st.getString() + "\n";
			break;
		case NUMBER:
			JsonNumber num = (JsonNumber) tree;
			s += "NUMBER " + num.toString() + "\n";
			break;
		case TRUE:
		case FALSE:
		case NULL:
			s += tree.getValueType().toString() + "\n";
			break;
		}
	}
	
	private void navigateDefinition(JsonValue tree, String key) {
		switch (tree.getValueType()) {
		case OBJECT:
			s += "OBJECT\n";
			JsonObject object = (JsonObject) tree;
			for (String name : object.keySet())
				navigateDefinition(object.get(name), name);
			break;
		case ARRAY:
			s += "ARRAY\n";
			JsonArray array = (JsonArray) tree;
			for (JsonValue val : array)
				navigateDefinition(val, null);
			break;
		case STRING:
			JsonString st = (JsonString) tree;
			s += String.format("private %s %s;[%s]\n", "String", key, st.getString());
			break;
		case NUMBER:
			JsonNumber num = (JsonNumber) tree;
			s += String.format("private %s %s;[%s]\n", "int", key, num.toString());
			break;
		case TRUE:
		case FALSE:
			s += String.format("private %s %s;[%s]\n", "boolean", key, tree.getValueType().toString());
			break;
		case NULL:
			s += String.format("private %s %s;[%s]\n", "String", key, tree.getValueType().toString());
			break;
		}
	}
	
	protected abstract String hack(Object[] params);

	@Override
	public String toString() {
		return getTree();
	}
	
	public String getTree() {
		if (js == null)
			return "[EMPTY]";
		s = "";
		navigateTree(js, null);
		return s;
	}
	
	public String getDefinition() {
		if (js == null)
			return "[EMPTY]";
		s = "";
		navigateDefinition(js, null);
		return s;
	}
	
	public String getContent() {
		return content;
	}
	
	
	protected interface Filter {
		public String filter(String json);
	}
	
	public static class Error {
		private String command;
		private String message;
		private String detail;
		private int code;
		
		public String getCommand() {
			return command;
		}
		public void setCommand(String command) {
			this.command = command;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getDetail() {
			return detail;
		}
		public void setDetail(String detail) {
			this.detail = detail;
		}
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		
		@Override
		public String toString() {
			String s = "";
			s += String.format("%30s: %s",
					"command", command);
			s += String.format("%30s: %s",
					"message", message);
			s += String.format("%30s: %d",
					"code", code);
			return s;
		}
	}
	
	private Error error;
	
	public Error getError() {
		return error;
	}
	public void setError(Error error) {
		this.error = error;
	}
	
	protected void checkUserException(int[] codes) throws BTSUserException {
		if (error != null) {
			for (int code : codes) {
				if (error.getCode() == code)
					throw new BTSUserException(error);
			}
		}
	}
	
	protected void checkSystemException() throws BTSSystemException {
		if (error != null) {
			error.setCommand(getClass().getSimpleName());
			throw new BTSSystemException(error);
		}
	}
	
	protected static Integer getInteger(Object obj) {
		if (obj instanceof Integer)
			return (Integer) obj;
		if (obj instanceof Long)
			return new Integer(((Long) obj).intValue());
		return null;
	}
	
	protected static Long getLong(Object obj) {
		if (obj instanceof Long)
			return (Long) obj;
		if (obj instanceof Integer)
			return new Long(((Integer) obj).longValue());
		return null;
	}
}