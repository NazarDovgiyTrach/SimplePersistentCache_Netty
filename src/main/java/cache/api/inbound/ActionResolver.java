package cache.api.inbound;

import cache.api.outbound.ResponseData;
import cache.store.KeyValueRepository;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ActionResolver {
  private static final Logger LOG = LogManager.getLogger(ActionResolver.class);
  private static final String SUCCESS = "success";
  private static final String FAILURE = "failure";

  private ActionResolver() {}

  public static ResponseData resolve(
      RequestData requestData, KeyValueRepository<String, Byte[]> keyValueRepository) {

    ResponseData responseData = new ResponseData(SUCCESS);
    Action action = Action.valueOf(requestData.getAction().toUpperCase());
    try {
      switch (action) {
        case CREATE:
          LOG.info("Perform {} action", action.name());
          keyValueRepository.save(
              requestData.getStringValue(), ArrayUtils.toObject(requestData.getBinaryValue()));
          break;
        case READ:
          LOG.info("Perform {} action", action.name());
          byte[] binary =
              ArrayUtils.toPrimitive(keyValueRepository.find(requestData.getStringValue()));

          responseData.setBinaryValue(binary);
          break;
        case DELETE:
          LOG.info("Perform {} action", action.name());
          keyValueRepository.delete(requestData.getStringValue());

          break;
        default:
          LOG.warn("Unsupported action: {}", action.name());
          responseData.setStatus(FAILURE);
      }
    } catch (Exception e) {
      LOG.error("Error performing {} action", action.name());
      responseData = new ResponseData(FAILURE, e.getMessage());
    }
    return responseData;
  }
}