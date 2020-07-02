package it.brunorenz.mybank.mybankconfiguration.bean;

import java.util.ArrayList;
import java.util.List;

import it.brunorenz.mybank.mybankconfiguration.network.IDataContainer;

public class GenericDataContainer implements IDataContainer {
    private List<String> excludedPackage;

    public List<String> getExcludedPackage() {
        if (excludedPackage == null) excludedPackage = new ArrayList<>();
        return excludedPackage;
    }
}
