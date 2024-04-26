package grimoire.grimoire.nick;

import com.dispute.nick.client.ClientNickManager;
import com.dispute.nick.nick.EntityNick;
import com.dispute.nick.nick.NickManager;
import grimoire.grimoire.data.GrimoireData;

import java.util.UUID;
import java.util.regex.Pattern;

public class OurClientNickMan extends ClientNickManager {

    static {
        NickManager.setInstance(new OurClientNickMan());
    }

    public static void init() {
    }

    @Override
    public void setNick(UUID id, EntityNick nick) {
        super.setNick(id, nick);
        final String nickStr = nick.nick();
        nickStr.replaceAll("§[0-9a-z]", "");
        var matcher = Pattern.compile("\\[([0-9]+)号]").matcher(nickStr);
        if (matcher.find()) {
            var i =matcher.group(1);
            GrimoireData.PlayerNickNameChange(Integer.parseInt(i), nickStr);
        }
    }
}
