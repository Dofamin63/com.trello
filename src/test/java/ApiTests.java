import core.models.ResponseModel;
import core.Specifications;
import core.models.RequestModel;
import configurations.Config;
import io.qameta.allure.Step;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import dataprovider.Dataprovider;
import dataprovider.RequestData;
import utils.DateUtils;

import java.util.HashMap;
import java.util.Map;

import static constants.EndPoints.*;
import static constants.IdConstants.*;

@Test(dataProvider = "dataprovider", dataProviderClass = Dataprovider.class)

public class ApiTests extends BaseTest {

    Map<String, String> contextHolder = new HashMap<>();

    @BeforeClass
    public void setup() {
        Specifications.installSpecification(Specifications.requestSpec(
                        Config.apiConfigurations().getTrelloUrl(),
                        Config.apiConfigurations().getBasePath(),
                        Config.getDbConfigurations().getUserName()),
                Specifications.responseSpec(200));
    }

    @Step("Создать доску")
    @Test(priority = 1)
    public void createBoard(RequestData data) {
        RequestModel request = RequestModel.builder().name(data.getNameBoard()).build();
        ResponseModel response = apiManager.create(ENDPOINT_BOARD, request);
        contextHolder.put(ID_BOARD, response.getId());
        Assert.assertEquals(response.getName(), data.getNameBoard());
    }

    @Step("Создать колонку")
    @Test(priority = 2)
    public void createLists(RequestData data) {
        RequestModel request = RequestModel.builder().name(data.getNameColumn()).idBoard(contextHolder.get(ID_BOARD)).build();
        ResponseModel response = apiManager.create(ENDPOINT_LIST, request);
        contextHolder.put(ID_LIST, response.getId());
        Assert.assertEquals(response.getName(), data.getNameColumn());
    }

    @Step("Создать карточку")
    @Test(priority = 3)
    public void createCards(RequestData data) {
        RequestModel request = RequestModel.builder().name(data.getNameCards()).idList(contextHolder.get(ID_LIST)).build();
        ResponseModel response = apiManager.create(ENDPOINT_CREATE_CARD, request);
        contextHolder.put(ID_CARD, response.getId());
        Assert.assertEquals(response.getName(), data.getNameCards());
    }

    @Step("Добавить вложение в карточку")
    @Test(priority = 4)
    public void addAttachment(RequestData data) {
        ResponseModel response = apiManager.addAttachment(ENDPOINT_CARD + contextHolder.get(ID_CARD) + ENDPOINT_ATTACHMENT, data.getPathAttachment());
        contextHolder.put(ID_FILE, response.getId());
        Assert.assertEquals(response.getName(), data.getNameAttachment());
    }

    @Step("Задать дедлайн карточки")
    @Test(priority = 5)
    public void addDeadlineCards(RequestData data) {
        String date = DateUtils.getData(data.getDate());
        RequestModel request = RequestModel.builder().due(date).build();
        ResponseModel response = apiManager.update(ENDPOINT_CARD + contextHolder.get(ID_CARD), request);
        contextHolder.put(ID_DEADLINE, response.getId());
        Assert.assertEquals(response.getDue(), date);
    }

    @Step("Добавить описание карточки")
    @Test(priority = 6)
    public void addDescriptionCards(RequestData data) {
        RequestModel request = RequestModel.builder().desc(data.getDesc()).build();
        ResponseModel response = apiManager.update(ENDPOINT_CARD + contextHolder.get(ID_CARD), request);
        contextHolder.put(ID_DESC, response.getId());
        Assert.assertEquals(response.getDesc(), data.getDesc());
    }

    @Step("Создать чек-лист")
    @Test(priority = 7)
    public void createChecklist(RequestData data) {
        RequestModel request = RequestModel.builder().name(data.getNameChecklist()).build();
        ResponseModel response = apiManager.create(ENDPOINT_CREATE_CHECKLIST, request, request.createParam(ID_PARAM, contextHolder.get(ID_CARD)));
        contextHolder.put(ID_CHECKLIST, response.getId());
        Assert.assertEquals(response.getName(), data.getNameChecklist());
    }

    @Step("Добавить пункт в чек-лист")
    @Test(priority = 8)
    public void addCheckItem(RequestData data) {
        RequestModel request = RequestModel.builder().name(data.getNameCheckItem()).build();
        ResponseModel response = apiManager.create(ENDPOINT_CREATE_CHECKITEM, request, request.createParam(ID_PARAM, contextHolder.get(ID_CHECKLIST)));
        contextHolder.put(ID_CHECKITEM, response.getId());
        Assert.assertEquals(response.getName(), data.getNameCheckItem());
    }

    @Step("Добавить второй пункт в чек-лист")
    @Test(priority = 8)
    public void addCheckItemTwo(RequestData data) {
        RequestModel request = RequestModel.builder().name(data.getNameCheckItemTwo()).build();
        ResponseModel response = apiManager.create(ENDPOINT_CREATE_CHECKITEM, request, request.createParam(ID_PARAM, contextHolder.get(ID_CHECKLIST)));
        contextHolder.put(ID_CHECKITEM_TWO, response.getId());
        Assert.assertEquals(response.getName(), data.getNameCheckItemTwo());
    }

    @Step("Выполнить первый пункт чек-листа")
    @Test(priority = 10)
    public void checkItemComplete(RequestData data) {
        RequestModel request = RequestModel.builder().state(data.getComplete()).build();
        ResponseModel response = apiManager.update(ENDPOINT_UPDATE_CHECKITEM + contextHolder.get(ID_CHECKITEM),
                request, request.createParam(ID_PARAM, contextHolder.get(ID_CARD)));
        Assert.assertEquals(response.getName(), data.getNameCheckItem());
        Assert.assertEquals(response.getState(), data.getComplete());
    }

    @Step("Создать вторую колонку")
    @Test(priority = 11)
    public void createListsTwo(RequestData data) {
        RequestModel request = RequestModel.builder().name(data.getNameBoardTwo()).idBoard(contextHolder.get(ID_BOARD)).build();
        ResponseModel response = apiManager.create(ENDPOINT_LIST, request);
        contextHolder.put(ID_CHECKLIST_TWO, response.getId());
        Assert.assertEquals(response.getName(), data.getNameBoardTwo());
    }

    @Step("Переместить карточку")
    @Test(priority = 12)
    public void moveCards(RequestData data) {
        RequestModel request = RequestModel.builder().idList(contextHolder.get(ID_CHECKLIST_TWO)).build();
        apiManager.update(ENDPOINT_CARD + contextHolder.get(ID_CARD), request);
    }

    @Step("Архивировать колонку")
    @Test(priority = 13)
    public void archiveList(RequestData data) {
        RequestModel request = RequestModel.builder().closed(data.getArchive()).build();
        ResponseModel response = apiManager.update(ENDPOINT_LIST + "/" + contextHolder.get(ID_LIST), request);
        Assert.assertEquals(response.getName(), data.getNameColumn());
        Assert.assertEquals(response.getClosed(), data.getArchive());
    }

    @Step("Выполнить второй пункт чек-листа")
    @Test(priority = 14)
    public void checkItemCompleteTwo(RequestData data) {
        RequestModel request = RequestModel.builder().state(data.getComplete()).build();
        ResponseModel response = apiManager.update(ENDPOINT_UPDATE_CHECKITEM + contextHolder.get(ID_CHECKITEM_TWO),
                request, request.createParam(ID_PARAM, contextHolder.get(ID_CARD)));
        Assert.assertEquals(response.getName(), data.getNameCheckItemTwo());
        Assert.assertEquals(response.getState(), data.getComplete());
    }

    @Step("Добавить коментарий")
    @Test(priority = 15)
    public void addComments(RequestData data) {
        RequestModel request = RequestModel.builder().text(data.getText()).build();
        ResponseModel response = apiManager.create(ENDPOINT_ADD_COMMENTS, request, request.createParam(ID_PARAM, contextHolder.get(ID_CARD)));
        contextHolder.put(ID_COMMENTS, response.getId());
        Assert.assertEquals(response.getData().getText(), data.getText());
    }

    @Step("Удалить доску")
    @Test(priority = 16)
    public void deleteBoard(RequestData data) {
        apiManager.delete(ENDPOINT_BOARD + "/" + contextHolder.get(ID_BOARD));
    }
}
