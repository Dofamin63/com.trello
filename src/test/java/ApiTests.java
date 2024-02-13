import core.ApiManager;
import core.models.ResponseModel;
import core.Specifications;
import core.models.RequestModel;
import configurations.Config;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import dataprovider.Dataprovider;
import dataprovider.RequestData;
import utils.DateUtils;

import java.util.HashMap;
import java.util.Map;

import static constants.ApiConstants.*;
import static constants.IdConstants.*;

public class ApiTests {

    ApiManager apiManager = new ApiManager();
    Map<String, String> contextHolder = new HashMap<>();

    @BeforeTest
    public void setup() {
        Specifications.installSpecification(Specifications.requestSpec(
                        Config.baseConfigurations().getTrelloUrl(),
                        Config.baseConfigurations().getBasePath(),
                        Config.baseConfigurations().getDbUserName()),
                Specifications.responseSpec(200));
    }

    @Test(priority = 1, description = "Создать доску", dataProvider = "dataprovider", dataProviderClass = Dataprovider.class)
    public void createBoard(RequestData data) {
        RequestModel request = RequestModel.builder().name(data.getNameBoard()).build();
        ResponseModel response = apiManager.create(ENDPOINT_BOARD, request);
        contextHolder.put(BOARD_ID, response.getId());
        Assert.assertEquals(response.getName(), data.getNameBoard());
    }

    @Test(priority = 2, description = "Создать колонку", dataProvider = "dataprovider", dataProviderClass = Dataprovider.class)
    public void createLists(RequestData data) {
        RequestModel request = RequestModel.builder().name(data.getNameColumn()).idBoard(contextHolder.get(BOARD_ID)).build();
        ResponseModel response = apiManager.create(ENDPOINT_LIST, request);
        contextHolder.put(LIST_ID, response.getId());
        Assert.assertEquals(response.getName(), data.getNameColumn());
    }

    @Test(priority = 3, description = "Создать карточку", dataProvider = "dataprovider", dataProviderClass = Dataprovider.class)
    public void createCards(RequestData data) {
        RequestModel request = RequestModel.builder().name(data.getNameCards()).idList(contextHolder.get(LIST_ID)).build();
        ResponseModel response = apiManager.create(ENDPOINT_CREATE_CARD, request);
        contextHolder.put(CARD_ID, response.getId());
        Assert.assertEquals(response.getName(), data.getNameCards());
    }

    @Test(priority = 4, description = "Добавить вложение в карточку", dataProvider = "dataprovider", dataProviderClass = Dataprovider.class)
    public void addAttachment(RequestData data) {
        ResponseModel response = apiManager.addAttachment(ENDPOINT_CARD + contextHolder.get(CARD_ID) + ENDPOINT_ATTACHMENT, data.getPathAttachment());
        contextHolder.put(FILE_ID, response.getId());
        Assert.assertEquals(response.getName(), data.getNameAttachment());
    }

    @Test(priority = 5, description = "Задать дедлайн карточки", dataProvider = "dataprovider", dataProviderClass = Dataprovider.class)
    public void addDeadlineCards(RequestData data) {
        String date = DateUtils.getData(data.getDate());
        RequestModel request = RequestModel.builder().due(date).build();
        ResponseModel response = apiManager.update(ENDPOINT_CARD + contextHolder.get(CARD_ID), request);
        contextHolder.put(DEADLINE_ID, response.getId());
        Assert.assertEquals(response.getDue(), date);
    }

    @Test(priority = 6, description = "Добавить описание карточки", dataProvider = "dataprovider", dataProviderClass = Dataprovider.class)
    public void addDescriptionCards(RequestData data) {
        RequestModel request = RequestModel.builder().desc(data.getDesc()).build();
        ResponseModel response = apiManager.update(ENDPOINT_CARD + contextHolder.get(CARD_ID), request);
        contextHolder.put(DESC_ID, response.getId());
        Assert.assertEquals(response.getDesc(), data.getDesc());
    }

    @Test(priority = 7, description = "Создать чек-лист", dataProvider = "dataprovider", dataProviderClass = Dataprovider.class)
    public void createChecklist(RequestData data) {
        RequestModel request = RequestModel.builder().name(data.getNameChecklist()).build();
        ResponseModel response = apiManager.create(ENDPOINT_CREATE_CHECKLIST, request, request.createParam(PARAM_ID, contextHolder.get(CARD_ID)));
        contextHolder.put(CHECKLIST_ID, response.getId());
        Assert.assertEquals(response.getName(), data.getNameChecklist());
    }

    @Test(priority = 8, description = "Добавить пункт в чек-лист", dataProvider = "dataprovider", dataProviderClass = Dataprovider.class)
    public void addCheckItem(RequestData data) {
        RequestModel request = RequestModel.builder().name(data.getNameCheckItem()).build();
        ResponseModel response = apiManager.create(ENDPOINT_CREATE_CHECKITEM, request, request.createParam(PARAM_ID, contextHolder.get(CHECKLIST_ID)));
        contextHolder.put(CHECKITEM_ID, response.getId());
        Assert.assertEquals(response.getName(), data.getNameCheckItem());
    }

    @Test(priority = 8, description = "Добавить второй пункт в чек-лист", dataProvider = "dataprovider", dataProviderClass = Dataprovider.class)
    public void addCheckItemTwo(RequestData data) {
        RequestModel request = RequestModel.builder().name(data.getNameCheckItemTwo()).build();
        ResponseModel response = apiManager.create(ENDPOINT_CREATE_CHECKITEM, request, request.createParam(PARAM_ID, contextHolder.get(CHECKLIST_ID)));
        contextHolder.put(CHECKITEM_TWO_ID, response.getId());
        Assert.assertEquals(response.getName(), data.getNameCheckItemTwo());
    }

    @Test(priority = 10, description = "Выполнить первый пункт чек-листа", dataProvider = "dataprovider", dataProviderClass = Dataprovider.class)
    public void checkItemComplete(RequestData data) {
        RequestModel request = RequestModel.builder().state(data.getComplete()).build();
        ResponseModel response = apiManager.update(ENDPOINT_UPDATE_CHECKITEM + contextHolder.get(CHECKITEM_ID),
                request, request.createParam(PARAM_ID, contextHolder.get(CARD_ID)));
        Assert.assertEquals(response.getName(), data.getNameCheckItem());
        Assert.assertEquals(response.getState(), data.getComplete());
    }

    @Test(priority = 11, description = "Создать вторую колонку", dataProvider = "dataprovider", dataProviderClass = Dataprovider.class)
    public void createListsTwo(RequestData data) {
        RequestModel request = RequestModel.builder().name(data.getNameBoardTwo()).idBoard(contextHolder.get(BOARD_ID)).build();
        ResponseModel response = apiManager.create(ENDPOINT_LIST, request);
        contextHolder.put(LIST_TWO_ID, response.getId());
        Assert.assertEquals(response.getName(), data.getNameBoardTwo());
    }

    @Test(priority = 12, description = "Переместить карточку")
    public void moveCards() {
        RequestModel request = RequestModel.builder().idList(contextHolder.get(LIST_TWO_ID)).build();
        ResponseModel response = apiManager.update(ENDPOINT_CARD + contextHolder.get(CARD_ID), request);
        Assert.assertEquals(response.getIdList(), contextHolder.get(LIST_TWO_ID));
    }

    @Test(priority = 13, description = "Архивировать колонку", dataProvider = "dataprovider", dataProviderClass = Dataprovider.class)
    public void archiveList(RequestData data) {
        RequestModel request = RequestModel.builder().closed(data.getArchive()).build();
        ResponseModel response = apiManager.update(ENDPOINT_LIST + "/" + contextHolder.get(LIST_ID), request);
        Assert.assertEquals(response.getName(), data.getNameColumn());
        Assert.assertEquals(response.getClosed(), data.getArchive());
    }

    @Test(priority = 14, description = "Выполнить второй пункт чек-листа", dataProvider = "dataprovider", dataProviderClass = Dataprovider.class)
    public void checkItemCompleteTwo(RequestData data) {
        RequestModel request = RequestModel.builder().state(data.getComplete()).build();
        ResponseModel response = apiManager.update(ENDPOINT_UPDATE_CHECKITEM + contextHolder.get(CHECKITEM_TWO_ID),
                request, request.createParam(PARAM_ID, contextHolder.get(CARD_ID)));
        Assert.assertEquals(response.getName(), data.getNameCheckItemTwo());
        Assert.assertEquals(response.getState(), data.getComplete());
    }

    @Test(priority = 15, description = "Добавить коментарий", dataProvider = "dataprovider", dataProviderClass = Dataprovider.class)
    public void addComments(RequestData data) {
        RequestModel request = RequestModel.builder().text(data.getText()).build();
        ResponseModel response = apiManager.create(ENDPOINT_ADD_COMMENTS, request, request.createParam(PARAM_ID, contextHolder.get(CARD_ID)));
        contextHolder.put(COMMENTS_ID, response.getId());
        Assert.assertEquals(response.getData().getText(), data.getText());
    }

    @Test(priority = 16, description = "Удалить доску")
    public void deleteBoard() {
        ResponseModel response = apiManager.delete(ENDPOINT_BOARD + "/" + contextHolder.get(BOARD_ID));
        Assert.assertNull(response.get_value());
    }
}
