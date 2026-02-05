package stepDefinitions;

import au.com.telstra.simcardactivator.SimCardActivator;
import au.com.telstra.simcardactivator.foundation.ActuationResult;
import au.com.telstra.simcardactivator.foundation.SimCard;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = SimCardActivator.class, loader = SpringBootContextLoader.class)
public class SimCardActivatorStepDefinitions {
    @Autowired
    private TestRestTemplate restTemplate;

    private SimCard requestSimCard;
    private SimCard queriedSimCard;

    @Given("a sim card with iccid {string} and customer email {string}")
    public void aSimCardWithIccidAndCustomerEmail(String iccid, String customerEmail) {
        requestSimCard = new SimCard(iccid, customerEmail, false);
    }

    @When("I submit an activation request")
    public void iSubmitAnActivationRequest() {
        restTemplate.postForObject("http://localhost:8080/activate", requestSimCard, ActuationResult.class);
    }

    @When("I query activation with id {long}")
    public void iQueryActivationWithId(long simCardId) {
        queriedSimCard = restTemplate.getForObject(
                "http://localhost:8080/query?simCardId=" + simCardId,
                SimCard.class
        );
    }

    @Then("the activation should be successful")
    public void theActivationShouldBeSuccessful() {
        Assertions.assertNotNull(queriedSimCard, "Query response should not be null");
        Assertions.assertTrue(queriedSimCard.getActive(), "Expected activation to be successful");
    }

    @Then("the activation should be unsuccessful")
    public void theActivationShouldBeUnsuccessful() {
        Assertions.assertNotNull(queriedSimCard, "Query response should not be null");
        Assertions.assertFalse(queriedSimCard.getActive(), "Expected activation to be unsuccessful");
    }
}
