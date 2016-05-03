package com.fz.us.dict.util;

/**
 * 工具类 - 系统配置
 * ============================================================================
 * @version 
 * ============================================================================
 */
public class SystemLabelUtil {
	public static final String CONFIG_FILE_NAME = "us_systemLabel.xml";// 系统配置文件名称
	public static final String SYSTEM_CONFIG_CACHE_KEY = "SystemLabel";// SystemLabel缓存Key

    /*public static void main(String args[]){
        SystemLabel sl = SystemLabelUtil.getSystemLabel();
        Node n = (Node)sl.getForms().get(SystemLabel.FormEnum.client);
        System.out.println(n.valueOf("@name"));
    }*/
	/**
	 * 获取系统配置信息
	 * 
	 * @return SystemLabel对象
	 */
	/*public static SystemLabel getSystemLabel() {
		SystemLabel systemLabel = null;//(SystemLabel) OsCacheConfigUtil.getFromCache(SYSTEM_CONFIG_CACHE_KEY);
		if (systemLabel != null) {
			return systemLabel;
		}
		InputStream labelStream = null;
		Document document = null;
		try {
			//String configFilePath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + CONFIG_FILE_NAME;
			//configFile = new File(configFilePath);
			SAXReader saxReader = new SAXReader();
			//document = saxReader.read(configFile);
            labelStream = SystemLabelUtil.class.getResourceAsStream("/" +CONFIG_FILE_NAME);
            document = saxReader.read(labelStream);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        Element manager = document.getRootElement();
        System.out.println(manager.attributeValue("version"));

        Node us = manager.selectSingleNode("us");
        System.out.println(us.valueOf("@test"));
        Node forms = us.selectSingleNode("forms");
        Node views = us.selectSingleNode("views");

        List<Node> formList = forms.selectNodes("form");
        System.out.println(formList.size());
        Node clientForm = forms.selectSingleNode("form[@name='client']");
        System.out.println(clientForm.valueOf("@name") + " " + clientForm.getPath());
        System.out.println(clientForm.selectSingleNode("fields/field[@name='name']").getText());

        Node field = clientForm.selectSingleNode("fields/field[@name='name']");
        System.out.println(field.valueOf("@name") + " " + field.getPath());
        //通过以下方式替换成最终唯一Id form -> [@name=client]
        //form - > client
        //field -> name
        String path = field.getPath() + "/";
        String key = path.replace("form/","client/").replace("field/","name");
        System.out.println(key);

        systemLabel = new SystemLabel();
        systemLabel.setVersion(1);
        Map formMap = new HashMap<SystemLabel.FormEnum,Node>();
        formMap.put(SystemLabel.FormEnum.client,forms.selectSingleNode("form[@name='"+ SystemLabel.FormEnum.client.name()+"']"));
        systemLabel.setForms(formMap);

		return systemLabel;
	}*/
	
	/**
	 * 更新系统配置信息
	 * 
	 * @param SystemLabel
	 *          SystemLabel对象
	 */
	/*public static void update(SystemLabel SystemLabel) {
		File configFile = null;
		Document document = null;
		try {
			String configFilePath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + CONFIG_FILE_NAME;
			configFile = new File(configFilePath);
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(configFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Element rootElement = document.getRootElement();
		Element SystemLabelElement = rootElement.element("SystemLabel");
		Node systemNameNode = document.selectSingleNode("/poa/SystemLabel/systemName");
		Node systemVersionNode = document.selectSingleNode("/poa/SystemLabel/systemVersion");
		Node systemDescriptionNode = document.selectSingleNode("/poa/SystemLabel/systemDescription");
		Node isInstalledNode = document.selectSingleNode("/poa/SystemLabel/isInstalled");
		Node shopNameNode = document.selectSingleNode("/poa/SystemLabel/shopName");
		Node shopUrlNode = document.selectSingleNode("/poa/SystemLabel/shopUrl");
		Node shopLogoNode = document.selectSingleNode("/poa/SystemLabel/shopLogo");
		Node hotSearchNode = document.selectSingleNode("/poa/SystemLabel/hotSearch");
		Node metaKeywordsNode = document.selectSingleNode("/poa/SystemLabel/metaKeywords");
		Node metaDescriptionNode = document.selectSingleNode("/poa/SystemLabel/metaDescription");
		Node addressNode = document.selectSingleNode("/poa/SystemLabel/address");
		Node phoneNode = document.selectSingleNode("/poa/SystemLabel/phone");
		Node zipCodeNode = document.selectSingleNode("/poa/SystemLabel/zipCode");
		Node emailNode = document.selectSingleNode("/poa/SystemLabel/email");
		Node currencyTypeNode = document.selectSingleNode("/poa/SystemLabel/currencyType");
		Node currencySignNode = document.selectSingleNode("/poa/SystemLabel/currencySign");
		Node currencyUnitNode = document.selectSingleNode("/poa/SystemLabel/currencyUnit");
		Node priceScaleNode = document.selectSingleNode("/poa/SystemLabel/priceScale");
		Node priceRoundTypeNode = document.selectSingleNode("/poa/SystemLabel/priceRoundType");
		Node orderScaleNode = document.selectSingleNode("/poa/SystemLabel/orderScale");
		Node orderRoundTypeNode = document.selectSingleNode("/poa/SystemLabel/orderRoundType");
		Node certtextNode = document.selectSingleNode("/poa/SystemLabel/certtext");
		Node storeAlertCountNode = document.selectSingleNode("/poa/SystemLabel/storeAlertCount");
		Node storeFreezeTimeNode = document.selectSingleNode("/poa/SystemLabel/storeFreezeTime");
		Node uploadLimitNode = document.selectSingleNode("/poa/SystemLabel/uploadLimit");
		Node isLoginFailureLockNode = document.selectSingleNode("/poa/SystemLabel/isLoginFailureLock");
		Node loginFailureLockCountNode = document.selectSingleNode("/poa/SystemLabel/loginFailureLockCount");
		Node loginFailureLockTimeNode = document.selectSingleNode("/poa/SystemLabel/loginFailureLockTime");
		Node isRegisterNode = document.selectSingleNode("/poa/SystemLabel/isRegister");
		Node watermarkImagePathNode = document.selectSingleNode("/poa/SystemLabel/watermarkImagePath");
		Node watermarkPositionNode = document.selectSingleNode("/poa/SystemLabel/watermarkPosition");
		Node watermarkAlphaNode = document.selectSingleNode("/poa/SystemLabel/watermarkAlpha");
		Node bigProductImageWidthNode = document.selectSingleNode("/poa/SystemLabel/bigProductImageWidth");
		Node bigProductImageHeightNode = document.selectSingleNode("/poa/SystemLabel/bigProductImageHeight");
		Node smallProductImageWidthNode = document.selectSingleNode("/poa/SystemLabel/smallProductImageWidth");
		Node smallProductImageHeightNode = document.selectSingleNode("/poa/SystemLabel/smallProductImageHeight");
		Node thumbnailProductImageWidthNode = document.selectSingleNode("/poa/SystemLabel/thumbnailProductImageWidth");
		Node thumbnailProductImageHeightNode = document.selectSingleNode("/poa/SystemLabel/thumbnailProductImageHeight");
		Node defaultBigProductImagePathNode = document.selectSingleNode("/poa/SystemLabel/defaultBigProductImagePath");
		Node defaultSmallProductImagePathNode = document.selectSingleNode("/poa/SystemLabel/defaultSmallProductImagePath");
		Node defaultThumbnailProductImagePathNode = document.selectSingleNode("/poa/SystemLabel/defaultThumbnailProductImagePath");
		Node allowedUploadImageExtensionNode = document.selectSingleNode("/poa/SystemLabel/allowedUploadImageExtension");
		Node allowedUploadMediaExtensionNode = document.selectSingleNode("/poa/SystemLabel/allowedUploadMediaExtension");
		Node allowedUploadFileExtensionNode = document.selectSingleNode("/poa/SystemLabel/allowedUploadFileExtension");
		Node smtpFromMailNode = document.selectSingleNode("/poa/SystemLabel/smtpFromMail");
		Node smtpHostNode = document.selectSingleNode("/poa/SystemLabel/smtpHost");
		Node smtpPortNode = document.selectSingleNode("/poa/SystemLabel/smtpPort");
		Node smtpUsernameNode = document.selectSingleNode("/poa/SystemLabel/smtpUsername");
		Node smtpPasswordNode = document.selectSingleNode("/poa/SystemLabel/smtpPassword");
		Node pointTypeNode = document.selectSingleNode("/poa/SystemLabel/pointType");
		Node pointScaleNode = document.selectSingleNode("/poa/SystemLabel/pointScale");
		
		if(systemNameNode == null){
			systemNameNode = SystemLabelElement.addElement("systemName");
		}
		if(systemVersionNode == null){
			systemVersionNode = SystemLabelElement.addElement("systemVersion");
		}
		if(systemDescriptionNode == null){
			systemDescriptionNode = SystemLabelElement.addElement("systemDescription");
		}
		if(isInstalledNode == null){
			isInstalledNode = SystemLabelElement.addElement("isInstalled");
		}
		if(shopNameNode == null){
			shopNameNode = SystemLabelElement.addElement("shopName");
		}
		if(shopUrlNode == null){
			shopUrlNode = SystemLabelElement.addElement("shopUrl");
		}
		if(shopLogoNode == null){
			shopLogoNode = SystemLabelElement.addElement("shopLogo");
		}
		if(hotSearchNode == null){
			hotSearchNode = SystemLabelElement.addElement("hotSearch");
		}
		if(metaKeywordsNode == null){
			metaKeywordsNode = SystemLabelElement.addElement("metaKeywords");
		}
		if(metaDescriptionNode == null){
			metaDescriptionNode = SystemLabelElement.addElement("metaDescription");
		}
		if(addressNode == null){
			addressNode = SystemLabelElement.addElement("address");
		}
		if(phoneNode == null){
			phoneNode = SystemLabelElement.addElement("phone");
		}
		if(zipCodeNode == null){
			zipCodeNode = SystemLabelElement.addElement("zipCode");
		}
		if(emailNode == null){
			emailNode = SystemLabelElement.addElement("email");
		}
		if(currencyTypeNode == null){
			currencyTypeNode = SystemLabelElement.addElement("currencyType");
		}
		if(currencySignNode == null){
			currencySignNode = SystemLabelElement.addElement("currencySign");
		}
		if(currencyUnitNode == null){
			currencyUnitNode = SystemLabelElement.addElement("currencyUnit");
		}
		if(priceScaleNode == null){
			priceScaleNode = SystemLabelElement.addElement("priceScale");
		}
		if(priceRoundTypeNode == null){
			priceRoundTypeNode = SystemLabelElement.addElement("priceRoundType");
		}
		if(orderScaleNode == null){
			orderScaleNode = SystemLabelElement.addElement("orderScale");
		}
		if(orderRoundTypeNode == null){
			orderRoundTypeNode = SystemLabelElement.addElement("orderRoundType");
		}
		if(certtextNode == null){
			certtextNode = SystemLabelElement.addElement("certtext");
		}
		if(storeAlertCountNode == null){
			storeAlertCountNode = SystemLabelElement.addElement("storeAlertCount");
		}
		if(storeFreezeTimeNode == null){
			storeFreezeTimeNode = SystemLabelElement.addElement("storeFreezeTime");
		}
		if(uploadLimitNode == null){
			uploadLimitNode = SystemLabelElement.addElement("uploadLimit");
		}
		if(isLoginFailureLockNode == null){
			isLoginFailureLockNode = SystemLabelElement.addElement("isLoginFailureLock");
		}
		if(loginFailureLockCountNode == null){
			loginFailureLockCountNode = SystemLabelElement.addElement("loginFailureLockCount");
		}
		if(loginFailureLockTimeNode == null){
			loginFailureLockTimeNode = SystemLabelElement.addElement("loginFailureLockTime");
		}
		if(isRegisterNode == null){
			isRegisterNode = SystemLabelElement.addElement("isRegister");
		}
		if(watermarkImagePathNode == null){
			watermarkImagePathNode = SystemLabelElement.addElement("watermarkImagePath");
		}
		if(watermarkPositionNode == null){
			watermarkPositionNode = SystemLabelElement.addElement("watermarkPosition");
		}
		if(watermarkAlphaNode == null){
			watermarkAlphaNode = SystemLabelElement.addElement("watermarkAlpha");
		}
		if(bigProductImageWidthNode == null){
			bigProductImageWidthNode = SystemLabelElement.addElement("bigProductImageWidth");
		}
		if(bigProductImageHeightNode == null){
			bigProductImageHeightNode = SystemLabelElement.addElement("bigProductImageHeight");
		}
		if(smallProductImageWidthNode == null){
			smallProductImageWidthNode = SystemLabelElement.addElement("smallProductImageWidth");
		}
		if(smallProductImageHeightNode == null){
			smallProductImageHeightNode = SystemLabelElement.addElement("smallProductImageHeight");
		}
		if(thumbnailProductImageWidthNode == null){
			thumbnailProductImageWidthNode = SystemLabelElement.addElement("thumbnailProductImageWidth");
		}
		if(thumbnailProductImageHeightNode == null){
			thumbnailProductImageHeightNode = SystemLabelElement.addElement("thumbnailProductImageHeight");
		}
		if(defaultBigProductImagePathNode == null){
			defaultBigProductImagePathNode = SystemLabelElement.addElement("defaultBigProductImagePath");
		}
		if(defaultSmallProductImagePathNode == null){
			defaultSmallProductImagePathNode = SystemLabelElement.addElement("defaultSmallProductImagePath");
		}
		if(defaultThumbnailProductImagePathNode == null){
			defaultThumbnailProductImagePathNode = SystemLabelElement.addElement("defaultThumbnailProductImagePath");
		}
		if(allowedUploadImageExtensionNode == null){
			allowedUploadImageExtensionNode = SystemLabelElement.addElement("allowedUploadImageExtension");
		}
		if(allowedUploadMediaExtensionNode == null){
			allowedUploadMediaExtensionNode = SystemLabelElement.addElement("allowedUploadMediaExtension");
		}
		if(allowedUploadFileExtensionNode == null){
			allowedUploadFileExtensionNode = SystemLabelElement.addElement("allowedUploadFileExtension");
		}
		if(smtpFromMailNode == null){
			smtpFromMailNode = SystemLabelElement.addElement("smtpFromMail");
		}
		if(smtpHostNode == null){
			smtpHostNode = SystemLabelElement.addElement("smtpHost");
		}
		if(smtpPortNode == null){
			smtpPortNode = SystemLabelElement.addElement("smtpPort");
		}
		if(smtpUsernameNode == null){
			smtpUsernameNode = SystemLabelElement.addElement("smtpUsername");
		}
		if(smtpPasswordNode == null){
			smtpPasswordNode = SystemLabelElement.addElement("smtpPassword");
		}
		if(pointTypeNode == null){
			pointTypeNode = SystemLabelElement.addElement("pointType");
		}
		if(pointScaleNode == null){
			pointScaleNode = SystemLabelElement.addElement("pointScale");
		}
		
		systemNameNode.setText(SystemLabel.getSystemName());
		systemVersionNode.setText(SystemLabel.getSystemVersion());
		systemDescriptionNode.setText(SystemLabel.getSystemDescription());
		isInstalledNode.setText(SystemLabel.getIsInstalled().toString());
		shopNameNode.setText(SystemLabel.getShopName());
		shopUrlNode.setText(StringUtils.removeEnd(SystemLabel.getShopUrl(), "/"));
		shopLogoNode.setText(SystemLabel.getShopLogo());
		hotSearchNode.setText(SystemLabel.getHotSearch());
		metaKeywordsNode.setText(SystemLabel.getMetaKeywords());
		metaDescriptionNode.setText(SystemLabel.getMetaDescription());
		addressNode.setText(SystemLabel.getAddress());
		phoneNode.setText(SystemLabel.getPhone());
		zipCodeNode.setText(SystemLabel.getZipCode());
		emailNode.setText(SystemLabel.getEmail());
		
		try {
			OutputFormat outputFormat = OutputFormat.createPrettyPrint();// 设置XML文档输出格式
			outputFormat.setEncoding("UTF-8");// 设置XML文档的编码类型
			outputFormat.setIndent(true);// 设置是否缩进
			outputFormat.setIndent("	");// 以TAB方式实现缩进
			outputFormat.setNewlines(true);// 设置是否换行
			XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(configFile), outputFormat);
			xmlWriter.write(document);
			xmlWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//OsCacheConfigUtil.flushEntry(SYSTEM_CONFIG_CACHE_KEY);
	}*/

	
	
}