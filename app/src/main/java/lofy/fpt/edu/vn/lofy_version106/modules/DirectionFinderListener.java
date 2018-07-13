package lofy.fpt.edu.vn.lofy_version106.modules;

import java.util.List;

import lofy.fpt.edu.vn.lofy_version106.entities.Route;


public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
    void onDirectionFinderSuccessGray(List<Route> route);
}
