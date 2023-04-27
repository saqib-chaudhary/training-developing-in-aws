package com.tpicap.cms.lambda.handlers;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tpicap.cms.AppConfig;
import com.tpicap.cms.DBOps;

public class DeleteHandler implements RequestHandler<Map<String, String>, Integer> {

    @Override
    public Integer handleRequest(Map<String, String> event, Context context) {
        try (DBOps db = new DBOps(new AppConfig())) {
            Integer noteId = Integer.valueOf(event.get("NoteId"));
            db.deleteItem(
                    event.get("UserId"),
                    noteId
            );
            return noteId;
        }
    }
}
