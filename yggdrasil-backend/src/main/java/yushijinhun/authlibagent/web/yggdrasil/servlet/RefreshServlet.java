package yushijinhun.authlibagent.web.yggdrasil.servlet;

import static yushijinhun.authlibagent.util.UUIDUtils.toUUID;
import java.util.UUID;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import yushijinhun.authlibagent.web.yggdrasil.AuthenticateResponse;

@WebServlet("/yggdrasil/refresh")
public class RefreshServlet extends YggdrasilPostServlet {

	private static final long serialVersionUID = 1L;

	@Value("#{config['feature.includeProfilesInRefresh']}")
	private boolean includeProfilesInRefresh;

	@Override
	protected JSONObject process(JSONObject req, HttpServletRequest rawReq) throws Exception {
		String clientToken = req.getString("clientToken");
		String accessToken = req.getString("accessToken");

		AuthenticateResponse auth;

		JSONObject newProfileReq = req.optJSONObject("selectedProfile");
		if (newProfileReq == null) {
			// refresh
			auth = backend.refresh(accessToken, clientToken);
		} else {
			// select profile
			UUID newProfile = toUUID(newProfileReq.getString("id"));
			auth = backend.selectProfile(accessToken, clientToken, newProfile);
		}

		return serializer.serializeAuthenticateResponse(auth, includeProfilesInRefresh);
	}

}
