
package com.djrapitops.plandemog;

import com.djrapitops.plan.UUIDFetcher;
import com.djrapitops.plan.command.hooks.Hook;
import com.djrapitops.plandemog.datautils.FileUtils;
import java.util.HashMap;
import java.util.UUID;

public class PlanHook implements Hook {

    private PlanDemographics plugin;
    
    public PlanHook(PlanDemographics p) {
        plugin = p;
    }
    
    @Override
    public HashMap<String, String> getData(String playerName) throws Exception {
        HashMap<String, String> data = new HashMap<>();
        UUID uuid = UUIDFetcher.getUUIDOf(playerName);
        data.put("DEM-GEOLOCATION", FileUtils.getGeolocation(uuid));
        data.put("DEM-SEX", FileUtils.getSex(uuid));
        data.put("DEM-GEOLOCATION", FileUtils.getAge(uuid));
        return data;
    }

    @Override
    public HashMap<String, String> getAllData(String playerName) throws Exception {
        return getData(playerName);
    }
    
}
