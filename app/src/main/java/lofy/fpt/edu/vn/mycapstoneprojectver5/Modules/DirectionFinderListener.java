package lofy.fpt.edu.vn.mycapstoneprojectver5.Modules;

import java.util.List;
import lofy.fpt.edu.vn.mycapstoneprojectver5.Entities.Route;


public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
    void onDirectionFinderSuccessGray(List<Route> route);
}
