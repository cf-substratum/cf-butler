package io.pivotal.cfapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.pivotal.cfapp.domain.Space;
import io.pivotal.cfapp.domain.UserSpaces;
import reactor.core.publisher.Mono;

@Service
public class UserSpacesService {

    private final SpaceUsersService service;

    @Autowired
    public UserSpacesService(SpaceUsersService service) {
        this.service = service;
    }

    public Mono<UserSpaces> getUserSpaces(String name) {
        return service
                .findByAccountName(name)
                .map(su -> new Space(su.getOrganization(), su.getSpace()))
                .collectList()
                .map(spaces -> UserSpaces.builder().accountName(name).spaces(spaces).build());
    }
}