package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService service;

    @GetMapping
    public Collection<Mpa> getAll() {
        log.info("Get request on all mpa");
        return service.getAllMpa();
    }

    @GetMapping("/{id}")
    public Mpa getMpaBYId(@PathVariable("id") Long id) {
        log.info("Get request on mpa by id={}", id);
        return service.getMpaById(id);
    }
}
