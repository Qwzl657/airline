package kg.attractor.airline.service;

import kg.attractor.airline.dto.CompanyCreateDto;
import kg.attractor.airline.dto.UserCreateDto;
import kg.attractor.airline.dto.UserDto;
import kg.attractor.airline.exception.CompanyFreezeException;
import kg.attractor.airline.exception.UserNotFoundException;
import kg.attractor.airline.model.Role;
import kg.attractor.airline.model.User;
import kg.attractor.airline.repository.BookingRepository;
import kg.attractor.airline.repository.UserRepository;
import kg.attractor.airline.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Пользователь не найден: " + email));
    }

    @Override
    @Transactional
    public void register(UserCreateDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException(
                    "Email уже зарегистрирован: " + dto.getEmail());
        }
        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .role(Role.USER)
                .enabled(true)
                .build();
        userRepository.save(user);
        log.info("Зарегистрирован новый пользователь: {}", dto.getEmail());
    }

    @Override
    public UserDto getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        "Пользователь не найден: " + email));
        return toDto(user);
    }

    @Override
    public Page<UserDto> getCompanies(Pageable pageable) {
        return userRepository.findByRole(Role.COMPANY, pageable)
                .map(this::toDtoWithBookingCount);
    }

    @Override
    @Transactional
    public void createCompany(CompanyCreateDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email уже занят: " + dto.getEmail());
        }
        User company = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .role(Role.COMPANY)
                .enabled(true)
                .build();
        userRepository.save(company);
        log.info("Создана компания: {}", dto.getFullName());
    }

    @Override
    @Transactional
    public void toggleFreeze(Long companyId) {
        User company = userRepository.findById(companyId)
                .orElseThrow(() -> new UserNotFoundException(
                        "Компания не найдена: " + companyId));

        if (company.isEnabled()) {
            boolean hasBookings =
                    bookingRepository.existsActiveBookingsByCompanyId(companyId);
            if (hasBookings) {
                throw new CompanyFreezeException(
                        "Нельзя заморозить компанию с активными бронированиями");
            }
            company.setEnabled(false);
            log.warn("Компания заморожена: id={}", companyId);
        } else {
            company.setEnabled(true);
            log.info("Компания разморожена: id={}", companyId);
        }
        userRepository.save(company);
    }

    private UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .logo(user.getLogo())
                .enabled(user.getEnabled())
                .build();
    }

    private UserDto toDtoWithBookingCount(User user) {
        long count = bookingRepository.countBookingsByCompanyId(user.getId());
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .logo(user.getLogo())
                .enabled(user.getEnabled())
                .bookingCount(count)
                .build();
    }
}