package com.river.accounts.service.impl;

import com.river.accounts.dto.AccountDto;
import com.river.accounts.dto.CardsDto;
import com.river.accounts.dto.CustomerDetailsDto;
import com.river.accounts.dto.LoansDto;
import com.river.accounts.entity.Account;
import com.river.accounts.entity.Customer;
import com.river.accounts.exception.ResourceNotFoundException;
import com.river.accounts.mapper.AccountMapper;
import com.river.accounts.mapper.CustomerMapper;
import com.river.accounts.repository.AccountRepository;
import com.river.accounts.repository.CustomerRepository;
import com.river.accounts.service.CustomerService;
import com.river.accounts.service.client.CardsFeignClient;
import com.river.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private AccountRepository accountRepository;

    private CustomerRepository customerRepository;

    private CardsFeignClient cardsFeignClient;

    private LoansFeignClient loansFeignClient;

    private CustomerMapper customerMapper;

    private AccountMapper accountMapper;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Account account = accountRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

        CardsDto cardsDto = cardsFeignClient.fetchCardDetails(mobileNumber).getBody();

        LoansDto loansDto = loansFeignClient.fetchLoanDetails(mobileNumber).getBody();

        CustomerDetailsDto customerDetailsDto = customerMapper.map(customer,  new CustomerDetailsDto());
        customerDetailsDto.setAccount(accountMapper.map(account, new AccountDto()));
        customerDetailsDto.setCard(cardsDto);
        customerDetailsDto.setLoan(loansDto);
        return customerDetailsDto;

    }
}
