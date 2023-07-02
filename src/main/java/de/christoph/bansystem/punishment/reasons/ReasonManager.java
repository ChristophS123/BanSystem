package de.christoph.bansystem.punishment.reasons;

import de.christoph.bansystem.BanSystem;
import de.christoph.bansystem.punishment.PunishmentType;

import java.util.ArrayList;

public class ReasonManager {

    private ArrayList<Reason> reasons;

    public ReasonManager() {
         reasons = new ArrayList<>();
         loadReasons();
    }

    private void loadReasons() {
        int maxReasons = 0;
        if(BanSystem.getInstance().getReasonFile().contains("MaxReasons")) {
            maxReasons = (int) BanSystem.getInstance().getReasonFile().get("MaxReasons");
        } else {
            createReasonExamples();
        }
        for(int i = 0; i <= maxReasons; i++) {
            if(BanSystem.getInstance().getReasonFile().contains("Reason" + i + ".Name")) {
                int id = i;
                String name = (String) BanSystem.getInstance().getReasonFile().get("Reason" + i + ".Name");
                long duration = Long.parseLong((String) BanSystem.getInstance().getReasonFile().get("Reason" + i + ".Duration"));
                PunishmentType punishmentType = PunishmentType.valueOf((String) BanSystem.getInstance().getReasonFile().get("Reason" + i + ".PunishmentType"));
                reasons.add(new Reason(id, name, duration, punishmentType));
            }
        }
        System.out.println(reasons);
    }

    private void createReasonExamples() {
        BanSystem.getInstance().getReasonFile().set("MaxReasons", 2);
        BanSystem.getInstance().getReasonFile().set("Reason1.Name", "Hacking");
        BanSystem.getInstance().getReasonFile().set("Reason1.Duration", "20000");
        BanSystem.getInstance().getReasonFile().set("Reason1.PunishmentType", PunishmentType.BAN.toString());
        BanSystem.getInstance().getReasonFile().set("Reason2.Name", "Chat");
        BanSystem.getInstance().getReasonFile().set("Reason2.Duration", "20000");
        BanSystem.getInstance().getReasonFile().set("Reason2.PunishmentType", PunishmentType.MUTE.toString());
    }

    public Reason getReasonByID(int id) {
        for(Reason reason : reasons) {
            if(reason.getId() == id)
                return reason;
        }
        return null;
    }

    public ArrayList<Reason> getReasons() {
        return reasons;
    }

}
