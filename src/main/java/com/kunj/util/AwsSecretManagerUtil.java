package com.kunj.util;//package com.kunj.util;
//
//
//import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
//import java.util.function.Consumer;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
//import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest.Builder;
//import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
//import software.amazon.awssdk.services.secretsmanager.model.SecretsManagerException;
//@Component
//@Slf4j
//public class AwsSecretManagerUtil {
//
//  public String secretManagerPropertiesMapping(String secretId,String secreteName){
//    GetSecretValueResponse secretValueResponse = null;
//    try(SecretsManagerClient secretsManagerClient = SecretsManagerClient.create ()){
//      /**
//       * call request ot secret Manager to get secretManager
//       */
//      GetSecretValueRequest secretValueRequest = new GetSecretValueRequest ();
//      secretValueRequest.setSecretId (secretId);
//
//        secretValueResponse = secretsManagerClient.getSecretValue(
//          (Consumer<Builder>) secretValueRequest);
//
//
//    }catch (SecretsManagerException e){
//      log.error(e.awsErrorDetails().errorMessage());
//    }
//    return secretValueResponse.secretString ();
//  }
//}
