package au.com.telstra.simcardactivator.component;

import au.com.telstra.simcardactivator.foundation.ActuationResult;
import au.com.telstra.simcardactivator.foundation.SimCard;
import au.com.telstra.simcardactivator.model.SimCardActivation;
import au.com.telstra.simcardactivator.repository.SimCardActivationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class SimCardActivationRestController {

    private final SimCardActuationHandler simCardActuationHandler;
    private final SimCardActivationRepository simCardActivationRepository;

    public SimCardActivationRestController(
            SimCardActuationHandler simCardActuationHandler,
            SimCardActivationRepository simCardActivationRepository
    ) {
        this.simCardActuationHandler = simCardActuationHandler;
        this.simCardActivationRepository = simCardActivationRepository;
    }

    @PostMapping(value = "/activate")
    public ActuationResult handleActivationRequest(@RequestBody SimCard simCard) {
        var actuationResult = simCardActuationHandler.actuate(simCard);
        var success = actuationResult != null && actuationResult.getSuccess();
        var activation = new SimCardActivation(simCard.getIccid(), simCard.getCustomerEmail(), success);
        simCardActivationRepository.save(activation);
        return actuationResult == null ? new ActuationResult(false) : actuationResult;
    }

    @GetMapping(value = "/query")
    public SimCard queryActivation(@RequestParam("simCardId") long simCardId) {
        var activation = simCardActivationRepository.findById(simCardId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sim card activation not found"
                ));
        return new SimCard(activation.getIccid(), activation.getCustomerEmail(), activation.isActive());
    }

}
