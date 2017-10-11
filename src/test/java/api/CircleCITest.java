package api;
import controllers.NetIDController;

import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;

import javax.validation.Validator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class CircleCITest {

    private final Validator validator = Validators.newValidator();

    @Test
    public void testNetID(){
        NetIDController testid = new NetIDController();
        testid.returnNetID();
        assertThat(validator.validate(testid), empty());
    }

}