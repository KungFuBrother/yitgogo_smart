package yitgogo.smart.tools;

public class SignatureTool {

	public static String key = "1111111111111111";

	public static String getSignature() {
		return Content.getStringContent("public_signature", "");
	}

	public static void saveSignature(String signature) {
		Content.saveStringContent("public_signature", signature);
	}

}
