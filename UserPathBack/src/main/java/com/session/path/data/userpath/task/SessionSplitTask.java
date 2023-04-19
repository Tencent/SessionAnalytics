//package com.session.path.data.userpath.task;
//
//import com.session.path.data.userpath.entity.UserLogUploadStatusEntity;
//import com.session.path.data.userpath.service.LogEventSessionService;
//import com.session.path.data.userpath.util.SpringContextUtil;
//import java.util.ArrayList;
//import java.util.List;
//import lombok.Getter;
//import lombok.Setter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @ClassName SessionSplitTask
// * @Description session切分
// * @Author author
// * @Date 2023/02/18 17:58
// * @Version 1.0
// **/
//@Component
//@Deprecated
//public class SessionSplitTask implements Runnable {
//
//    private static final Logger logger = LoggerFactory.getLogger(SessionSplitTask.class);
//
//    @Getter
//    @Setter
//    private UserLogUploadStatusEntity toTransfer;
//
//
//    @Autowired
//    LogEventSessionService logEventSessionService;
//
//    @Override
//    public void run() {
//        if (logEventSessionService == null) {
//            logEventSessionService = SpringContextUtil.getBean(LogEventSessionService.class);
//        }
//
//        logger.debug("线程开始:{}", toTransfer.toString());
//        List<UserLogUploadStatusEntity> taskEntitys = new ArrayList<>();
//        taskEntitys.add(toTransfer);
////        logEventSessionService.executeSessionTask(taskEntitys);
//        logger.debug("线程结束:{}", toTransfer.toString());
//    }
//}
