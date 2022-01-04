package com.tads.vdlab.service;

import com.tads.vdlab.controller.dto.UsuarioDTO;
import com.tads.vdlab.model.Role;
import com.tads.vdlab.model.Usuario;
import com.tads.vdlab.repository.RoleRepository;
import com.tads.vdlab.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, RoleRepository roleRepository){
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
    }

    public Page<UsuarioDTO> findPaginated(Pageable pageable) {

        Role roleParaAgendar = roleRepository.findByName("DISCENTE");
        List<UsuarioDTO> usuarios = usuarioRepository.findByRolesIsAndAtivo(roleParaAgendar, true)
                .stream().map(UsuarioDTO::toDTO).collect(Collectors.toList());

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<UsuarioDTO> list;

        if (usuarios.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, usuarios.size());
            list = usuarios.subList(startItem, toIndex);
        }

        Page<UsuarioDTO> pedidoPage
                = new PageImpl<UsuarioDTO>(list, PageRequest.of(currentPage, pageSize), usuarios.size());

        return pedidoPage;
    }

}
