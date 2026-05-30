package kg.attractor.airline.service;

import kg.attractor.airline.dto.CompanyCreateDto;
import kg.attractor.airline.dto.UserCreateDto;
import kg.attractor.airline.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    void register(UserCreateDto dto);

    UserDto getCurrentUser(String email);

    Page<UserDto> getCompanies(Pageable pageable);

    void createCompany(CompanyCreateDto dto);

    void toggleFreeze(Long companyId);
}