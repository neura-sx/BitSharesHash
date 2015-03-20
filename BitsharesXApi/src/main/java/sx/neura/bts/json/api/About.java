package sx.neura.bts.json.api;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class About extends CommandJson {

	public static class Result {
		private String blockchain_name;
		private String blockchain_description;
		private String client_version;
		private String bitshares_revision;
		private String bitshares_revision_age;
		private String fc_revision;
		private String fc_revision_age;
		private String compile_date;
		private String boost_version;
		private String openssl_version;
		private String build;
		private String jenkins_build_number;
		private String jenkins_build_url;
		public String getBlockchain_name() {
			return blockchain_name;
		}
		public void setBlockchain_name(String blockchain_name) {
			this.blockchain_name = blockchain_name;
		}
		public String getBlockchain_description() {
			return blockchain_description;
		}
		public void setBlockchain_description(String blockchain_description) {
			this.blockchain_description = blockchain_description;
		}
		public String getClient_version() {
			return client_version;
		}
		public void setClient_version(String client_version) {
			this.client_version = client_version;
		}
		public String getBitshares_revision() {
			return bitshares_revision;
		}
		public void setBitshares_revision(String bitshares_revision) {
			this.bitshares_revision = bitshares_revision;
		}
		public String getBitshares_revision_age() {
			return bitshares_revision_age;
		}
		public void setBitshares_revision_age(String bitshares_revision_age) {
			this.bitshares_revision_age = bitshares_revision_age;
		}
		public String getFc_revision() {
			return fc_revision;
		}
		public void setFc_revision(String fc_revision) {
			this.fc_revision = fc_revision;
		}
		public String getFc_revision_age() {
			return fc_revision_age;
		}
		public void setFc_revision_age(String fc_revision_age) {
			this.fc_revision_age = fc_revision_age;
		}
		public String getCompile_date() {
			return compile_date;
		}
		public void setCompile_date(String compile_date) {
			this.compile_date = compile_date;
		}
		public String getBoost_version() {
			return boost_version;
		}
		public void setBoost_version(String boost_version) {
			this.boost_version = boost_version;
		}
		public String getOpenssl_version() {
			return openssl_version;
		}
		public void setOpenssl_version(String openssl_version) {
			this.openssl_version = openssl_version;
		}
		public String getBuild() {
			return build;
		}
		public void setBuild(String build) {
			this.build = build;
		}
		public String getJenkins_build_number() {
			return jenkins_build_number;
		}
		public void setJenkins_build_number(String jenkins_build_number) {
			this.jenkins_build_number = jenkins_build_number;
		}
		public String getJenkins_build_url() {
			return jenkins_build_url;
		}
		public void setJenkins_build_url(String jenkins_build_url) {
			this.jenkins_build_url = jenkins_build_url;
		}
	}

	private Result result;

	public void setResult(Result result) {
		this.result = result;
	}

	public Result getResult() {
		return result;
	}
	
	private static final String COMMAND = "about";
	
	public static CommandJson test() {
		return doTest(COMMAND);
	}
	
	public static About.Result run() throws BTSSystemException {
		return run(0);
	}
	public static About.Result run(int id) throws BTSSystemException {
		About i = (About) new About().doRun(id, COMMAND);
		i.checkSystemException();
		return i.getResult();
	}

	public static void main(String[] args) {
		test();
//		try {
//			System.out.println(run().getBitshares_toolkit_revision());
//		} catch (BTSSystemException e) {
//			System.err.println(e.getError().getMessage());
//		}
	}
	
	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":{\"blockchain_name\":\"BitShares\",\"blockchain_description\":\"Life, Liberty, and Property for All\",\"client_version\":\"0.6.1\",\"bitshares_revision\":\"780030ed0813080e81737cc5bc79592ac28a0830\",\"bitshares_revision_age\":\"8 days ago\",\"fc_revision\":\"9c5450185a3d74b41f4a9722185a191bcbb2362a\",\"fc_revision_age\":\"12 days ago\",\"compile_date\":\"compiled on Feb  9 2015 at 21:27:57\",\"boost_version\":\"1.55\",\"openssl_version\":\"OpenSSL 1.0.1g 7 Apr 2014\",\"build\":\"win32 64-bit\",\"jenkins_build_number\":27,\"jenkins_build_url\":\"http://jenkins.syncad.com/job/bitshares_win_tags/PLATFORM=win64/27/\"}}";
	}
}
