package io.pivotal.cfapp.task;

import java.util.List;

import org.cloudfoundry.operations.DefaultCloudFoundryOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import io.pivotal.cfapp.domain.Organization;
import io.pivotal.cfapp.domain.Space;
import io.pivotal.cfapp.event.OrganizationsRetrievedEvent;
import io.pivotal.cfapp.event.SpacesRetrievedEvent;
import io.pivotal.cfapp.service.SpaceService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Component
public class SpacesTask implements ApplicationListener<OrganizationsRetrievedEvent> {

	private final DefaultCloudFoundryOperations opsClient;
    private final SpaceService service;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public SpacesTask(
    		DefaultCloudFoundryOperations opsClient,
    		SpaceService service,
    		ApplicationEventPublisher publisher) {
        this.opsClient = opsClient;
        this.service = service;
        this.publisher = publisher;
    }

	@Override
	public void onApplicationEvent(OrganizationsRetrievedEvent event) {
		collect(List.copyOf(event.getOrganizations()));
	}

	public void collect(List<Organization> organizations) {
        log.info("SpacesTask started");
        service
            .deleteAll()
            .thenMany(Flux.fromIterable(organizations))
            .flatMap(o -> getSpaces(o))
            .flatMap(service::save)
            .thenMany(service.findAll())
                .collectList()
                .subscribe(
                    result -> {
                        publisher.publishEvent(new SpacesRetrievedEvent(this).spaces(result));
                        log.info("SpacesTask completed");
                        log.trace("Retrieved {} spaces", result.size());
                    },
                    error -> {
                        log.error("SpacesTask terminated with error", error);
                    }
                );
    }

    protected Flux<Space> getSpaces(Organization organization) {
        return DefaultCloudFoundryOperations.builder()
			.from(opsClient)
			.organization(organization.getName())
            .build()
                .spaces()
                    .list()
                    .map(s -> new Space(organization.getName(), s.getName()));
    }

}