package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.MenuPane;
import sx.neura.bts.gui.view.components.display.DisplayField;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.PageDetails_Bts;
import sx.neura.bts.json.api.About;
import sx.neura.bts.json.api.GetInfo;
import sx.neura.bts.json.dto.Block;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Util;

public class Details_Status extends PageDetails_Bts<Block> {
	
	@FXML
	private MenuPane panoramaMenuUI;
	@FXML
	private LayerPane panoramaUI;
	@FXML
	private ToggleGroup panoramaToggleGroupUI;
	
	@FXML
	private DisplayText blockchain_name;
	@FXML
	private DisplayText blockchain_description;
	@FXML
	private DisplayText client_version;
	@FXML
	private DisplayText bitshares_revision;
	@FXML
	private DisplayText bitshares_revision_age;
	@FXML
	private DisplayText fc_revision;
	@FXML
	private DisplayText fc_revision_age;
	@FXML
	private DisplayText compile_date;
	@FXML
	private DisplayText boost_version;
	@FXML
	private DisplayText openssl_version;
	@FXML
	private DisplayText build;
	@FXML
	private DisplayText jenkins_build_number;
	@FXML
	private DisplayField jenkins_build_url;
	
	
	@FXML
	private DisplayText blockchain_head_block_num;
	@FXML
	private DisplayText blockchain_head_block_age;
	@FXML
	private DisplayText blockchain_head_block_timestamp;
	@FXML
	private DisplayText blockchain_average_delegate_participation;
	@FXML
	private DisplayText blockchain_confirmation_requirement;
	@FXML
	private DisplayText blockchain_share_supply;
	@FXML
	private DisplayText blockchain_blocks_left_in_round;
	@FXML
	private DisplayText blockchain_next_round_time;
	@FXML
	private DisplayText blockchain_next_round_timestamp;
	@FXML
	private DisplayText blockchain_random_seed;
	@FXML
	private DisplayText client_data_dir;
	@FXML
	private DisplayText client_version2;
	@FXML
	private DisplayText network_num_connections;
	@FXML
	private DisplayText network_num_connections_max;
	@FXML
	private DisplayText network_chain_downloader_running;
	@FXML
	private DisplayText network_chain_downloader_blocks_remaining;
	@FXML
	private DisplayText ntp_time;
	@FXML
	private DisplayText ntp_time_error;
	@FXML
	private DisplayText wallet_open;
	@FXML
	private DisplayText wallet_unlocked;
	@FXML
	private DisplayText wallet_unlocked_until;
	@FXML
	private DisplayText wallet_unlocked_until_timestamp;
	@FXML
	private DisplayText wallet_last_scanned_block_timestamp;
	@FXML
	private DisplayText wallet_scan_progress;
	@FXML
	private DisplayText wallet_block_production_enabled;
	@FXML
	private DisplayText wallet_next_block_production_time;
	@FXML
	private DisplayText wallet_next_block_production_timestamp;
	
	
	public Details_Status() {
		super();
	}
	
	@Override
	public String getTitle() {
		return "Status";
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		panoramaUI.setIndex(0);
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(0));
		
		Util.manageToggleGroup(panoramaToggleGroupUI);
	}	
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			
			{
				About.Result r;
				try {
					r = About.run();
				} catch (BTSSystemException e) {
					systemException(e);
					return;
				}
				blockchain_name.setText(r.getBlockchain_name());
				blockchain_description.setText(r.getBlockchain_description());
				client_version.setText(r.getClient_version());
				bitshares_revision.setText(r.getBitshares_revision());
				bitshares_revision_age.setText(r.getBitshares_revision_age());
				fc_revision.setText(r.getFc_revision());
				fc_revision_age.setText(r.getFc_revision_age());
				compile_date.setText(r.getCompile_date());
				boost_version.setText(r.getBoost_version());
				openssl_version.setText(r.getOpenssl_version());
				build.setText(r.getBuild());
				jenkins_build_number.setText(r.getJenkins_build_number());
				jenkins_build_url.setText(r.getJenkins_build_url());
			}
				
			{
				GetInfo.Result r;
				try {
					r = GetInfo.run();
				} catch (BTSSystemException e) {
					systemException(e);
					return;
				}
				
				blockchain_head_block_num.setText(new Integer(r.getBlockchain_head_block_num()).toString());
				blockchain_head_block_age.setText(new Integer(r.getBlockchain_head_block_age()).toString());
				blockchain_head_block_timestamp.setText(r.getBlockchain_head_block_timestamp());
				blockchain_average_delegate_participation.setText(new Integer(r.getBlockchain_average_delegate_participation()).toString());
				
				blockchain_confirmation_requirement.setText(new Integer(r.getBlockchain_confirmation_requirement()).toString());
				blockchain_share_supply.setText(new Long(r.getBlockchain_share_supply()).toString());
				blockchain_blocks_left_in_round.setText(new Integer(r.getBlockchain_blocks_left_in_round()).toString());
				blockchain_next_round_time.setText(new Integer(r.getBlockchain_next_round_time()).toString());
				blockchain_next_round_timestamp.setText(r.getBlockchain_next_round_timestamp());
				
				blockchain_random_seed.setText(r.getBlockchain_random_seed());
				client_data_dir.setText(r.getClient_data_dir());
				client_version2.setText(r.getClient_version());
				
				network_num_connections.setText(new Integer(r.getNetwork_num_connections()).toString());
				network_num_connections_max.setText(new Integer(r.getNetwork_num_connections_max()).toString());
				network_chain_downloader_running.setText(new Boolean(r.isNetwork_chain_downloader_running()).toString());
				network_chain_downloader_blocks_remaining.setText(r.getNetwork_chain_downloader_blocks_remaining());
				
//				ntp_time.setText(r.get);
//				ntp_time_error.setText(r.get);
//				wallet_open.setText(r.get);
//				wallet_unlocked.setText(r.get);
//				wallet_unlocked_until.setText(r.get);
//				wallet_unlocked_until_timestamp.setText(r.get);
//				wallet_last_scanned_block_timestamp.setText(r.get);
//				wallet_scan_progress.setText(r.get);
//				wallet_block_production_enabled.setText(r.get);
//				wallet_next_block_production_time.setText(r.get);		
//				wallet_next_block_production_timestamp.setText(r.get);
			}
			
		}
		super.loadData();
	}
	
	@FXML
	private void onPanoramaToggle01() {
		panoramaMenuUI.setIndex(0);
		applyPanoramaIndex(0);
	}
	@FXML
	private void onPanoramaToggle02() {
		panoramaMenuUI.setIndex(1);
		applyPanoramaIndex(1);
	}
	
	@FXML
	private void onPanoramaMenu01() {
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(0));
		applyPanoramaIndex(0);
	}
	@FXML
	private void onPanoramaMenu02() {
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(1));
		applyPanoramaIndex(1);
	}
	
	private void applyPanoramaIndex(int index) {
		panoramaUI.setIndex(index);
	}
	
}
