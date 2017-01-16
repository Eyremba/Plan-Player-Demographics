package com.djrapitops.plandemog;

import com.djrapitops.planlite.api.DataPoint;
import com.djrapitops.planlite.api.DataType;
import com.djrapitops.planlite.api.Hook;
import com.djrapitops.plandemog.datautils.FileUtils;
import java.util.HashMap;
import java.util.UUID;
import static org.bukkit.Bukkit.getOfflinePlayer;
import org.bukkit.OfflinePlayer;

public class PlanHook implements Hook {

    private PlanDemographics plugin;

    public PlanHook(PlanDemographics p) {
        plugin = p;
    }

    @Override
    public HashMap<String, DataPoint> getData(String playerName) throws Exception {
        HashMap<String, DataPoint> data = new HashMap<>();
        UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        if (uuid != null) {
            OfflinePlayer p = getOfflinePlayer(uuid);
            if (p.hasPlayedBefore()) {
                data.put("DEM-GEOLOCATION", new DataPoint(FileUtils.getGeolocation(uuid), DataType.OTHER));
                data.put("DEM-GENDER", new DataPoint(FileUtils.getGender(uuid), DataType.STRING));
                data.put("DEM-AGE", new DataPoint(FileUtils.getAge(uuid), DataType.AMOUNT));
            }
        }
        return data;
    }

    @Override
    public HashMap<String, DataPoint> getAllData(String playerName) throws Exception {
        return getData(playerName);
    }

}
