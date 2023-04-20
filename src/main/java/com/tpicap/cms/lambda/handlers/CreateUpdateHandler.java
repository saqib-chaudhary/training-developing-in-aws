package com.tpicap.cms.lambda.handlers;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tpicap.cms.AppConfig;
import com.tpicap.cms.DBOps;
import com.tpicap.cms.Note;

public class CreateUpdateHandler implements RequestHandler<Map<String, String>, Integer> {

    @Override
    public Integer handleRequest(Map<String, String> event, Context context) {
        try (DBOps db = new DBOps(new AppConfig())) {
            Note note = db.addItem(
                    event.get("UserId"),
                    Integer.valueOf(event.get("NoteId")),
                    event.get("Note"));
            return note.getNoteId();
        }

    }

}
