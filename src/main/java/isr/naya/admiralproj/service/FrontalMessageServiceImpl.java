package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.FrontalMessage;
import isr.naya.admiralproj.repository.FrontalMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontalMessageServiceImpl implements FrontalMessageService {
    @Autowired
    private FrontalMessageRepository frontalMessageRepository;

    @Override
    public List<FrontalMessage> getAll() {
        return frontalMessageRepository.findAll();
    }

    @Override
    public FrontalMessage save(FrontalMessage frontalMessage) {
        return frontalMessageRepository.save(frontalMessage);
    }

    @Override
    public void delete(Integer id) {
        frontalMessageRepository.delete(id);
    }
}
