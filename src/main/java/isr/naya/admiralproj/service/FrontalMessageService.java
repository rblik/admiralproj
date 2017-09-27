package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.FrontalMessage;

import java.util.List;

public interface FrontalMessageService {
    List<FrontalMessage> getAll();
    FrontalMessage save(FrontalMessage frontalMessage);
    void delete(Integer id);
}
