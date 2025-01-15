package com.airpapier.interfaces;

import com.airpapier.lib.Context;

import java.io.IOException;
import java.sql.SQLException;

public interface Handler {
    void handle(Context ctx) throws SQLException, IOException;
}