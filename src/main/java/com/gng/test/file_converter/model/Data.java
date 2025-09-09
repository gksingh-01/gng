package com.gng.test.file_converter.model;

import java.util.List;

public class Data {

    private List<DataItem> dataItems;

    public Data() {

    }

    public Data(List<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    public List<DataItem> getDataItems() {
        return dataItems;
    }

    public void setDataItems(List<DataItem> dataItems) {
        this.dataItems = dataItems;
    }
}
