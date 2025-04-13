package com.douzkj.zjjt.scheduler;

import com.douzkj.zjjt.repository.SignalRepository;
import com.douzkj.zjjt.repository.dao.Signal;
import com.douzkj.zjjt.service.SignalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SignalScheduler {

    @Autowired
    private  SignalRepository signalRepository;

    @Autowired
    private SignalService signalService;

    @Async("asyncExecutor")
    @Scheduled(fixedRate = 5000)
    public void closeTask() {
        log.info("entry closeTask ");
        List<Signal> signals = signalRepository.getOpenedButNotClosedSignals();
        for (Signal signal : signals) {
            log.info("openedButNotClosed. close signal. id={}", signal.getId());
            signalService.close(signal.getId());
        }
    }
}
