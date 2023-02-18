package com.github.sigrarr.lunisolarcalc.phenomena.local;

import com.github.sigrarr.lunisolarcalc.util.Titled;

public enum DiurnalPhase implements Titled {

    RISE("Rise"),
    TRANSIT("Transit"),
    SET("Set");

    private final String title;

    private DiurnalPhase(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
