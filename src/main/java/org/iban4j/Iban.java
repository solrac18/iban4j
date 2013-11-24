/*
 * Copyright 2013 Artur Mkrtchyan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.iban4j;

import org.iban4j.support.IbanStructure;
import org.iban4j.support.IbanStructureEntry;
import org.iban4j.support.IbanStructureResolver;

import java.io.Serializable;

/**
 * International Bank Account Number
 *
 * <a href="http://en.wikipedia.org/wiki/ISO_13616">ISO_13616</a>.
 */
public final class Iban implements Serializable {

    private static final long serialVersionUID = 3507561504372065317L;

    public static final String DEFAULT_CHECK_DIGIT = "00";

    private CountryCode countryCode;
    private String checkDigit;
    private String bankCode;
    private String branchCode;
    private String nationalCheckDigit;
    private String accountType;
    private String accountNumber;
    private String ownerAccountType;
    private String identificationNumber;

    private Iban(final Builder builder) throws IbanFormatException {

        this.countryCode = builder.countryCode;
        this.bankCode = builder.bankCode;
        this.branchCode = builder.branchCode;
        this.nationalCheckDigit = builder.nationalCheckDigit;
        this.accountType = builder.accountType;
        this.ownerAccountType = builder.ownerAccountType;
        this.accountNumber = builder.accountNumber;
        this.identificationNumber = builder.identificationNumber;

        checkDigit = DEFAULT_CHECK_DIGIT;
        checkDigit = IbanUtil.calculateCheckDigit(this);
        // TODO validation, add to next version
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public String getCheckDigit() {
        return checkDigit;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public String getNationalCheckDigit() {
        return nationalCheckDigit;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getOwnerAccountType() {
        return ownerAccountType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }



    // TODO for future releases
    private static Iban valueOf(final String iban) throws IbanFormatException {
        CountryCode countryCode = CountryCode.getByCode(iban.substring(0, 2));
        String checkDigit = iban.substring(2, 4);
//        return new Iban(countryCode, checkDigit, bban);
        return null;
    }

    protected String format() {
        IbanStructure structure = IbanStructureResolver.getStructure(countryCode.getAlpha2());
        return format(structure);
    }

    private String format(final IbanStructure structure) {
        StringBuilder sb = new StringBuilder();
        for(IbanStructureEntry entry : structure.getBbanEntries()) {
            switch (entry.getEntryType()) {

                case k:
                    break;
                case b:
                    sb.append(bankCode);
                    break;
                case s:
                    sb.append(branchCode);
                    break;
                case c:
                    sb.append(accountNumber);
                    break;
                case x:
                    sb.append(nationalCheckDigit);
                    break;
                case t:
                    sb.append(accountType);
                    break;
                case n:
                    sb.append(ownerAccountType);
                    break;
                case i:
                    sb.append(identificationNumber);
                    break;
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(countryCode.name())
                .append(checkDigit)
                .append(format())
                .toString();
    }

    /**
     * Iban Builder Class
     */
    public static class Builder {

        private CountryCode countryCode;
        private String checkDigit;
        private String bankCode;
        private String branchCode;
        private String nationalCheckDigit;
        private String accountType;
        private String accountNumber;
        private String ownerAccountType;
        private String identificationNumber;

        public Builder() {
        }

        public Builder countryCode(final CountryCode countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public Builder bankCode(final String bankCode) {
            this.bankCode = bankCode;
            return this;
        }

        public Builder branchCode(final String branchCode) {
            this.branchCode = branchCode;
            return this;
        }

        public Builder accountNumber(final String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder nationalCheckDigit(final String nationalCheckDigit) {
            this.nationalCheckDigit = nationalCheckDigit;
            return this;
        }

        public Builder accountType(final String accountType) {
            this.accountType = accountType;
            return this;
        }

        public Builder ownerAccountType(final String ownerAccountType) {
            this.ownerAccountType = ownerAccountType;
            return this;
        }

        public Builder identificationNumber(final String identificationNumber) {
            this.identificationNumber = identificationNumber;
            return this;
        }

        /**
         * Builds new iban instance
         *
         * @return new iban instance
         * @throws IbanFormatException if values are not parsable by Iban Specification
         * <a href="http://en.wikipedia.org/wiki/ISO_13616">ISO_13616</a>
         */
        public Iban build() throws IbanFormatException {
            return new Iban(this);
        }
    }

}
