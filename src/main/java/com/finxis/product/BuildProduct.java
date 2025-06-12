package com.finxis.product;

import cdm.base.datetime.*;
import cdm.base.datetime.daycount.DayCountFractionEnum;
import cdm.base.datetime.daycount.metafields.FieldWithMetaDayCountFractionEnum;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.base.math.DatedValue;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.QuantitySchedule;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.ReferenceWithMetaNonNegativeQuantitySchedule;
import cdm.base.staticdata.asset.common.*;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.PayerReceiver;
import cdm.observable.asset.*;
import cdm.observable.asset.metafields.ReferenceWithMetaPriceSchedule;
import cdm.product.asset.FixedRateSpecification;
import cdm.product.asset.FloatingRateSpecification;
import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.RateSpecification;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.PayRelativeToEnum;
import cdm.product.common.schedule.PaymentDates;
import cdm.product.common.schedule.RateSchedule;
import cdm.product.common.settlement.*;
import cdm.product.template.*;
import com.finxis.models.*;
import com.finxis.util.CdmDates;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.finxis.product.BuildProduct.*;
import com.finxis.util.CdmDates.*;
import com.rosetta.model.metafields.MetaFields;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BuildProduct {

  public Product createIrsOtcProduct(IrsOtcModel irsOtcModel) {

    CdmDates cdmDates = new CdmDates();

    Product product = Product.builder()
            .setNonTransferableProduct(NonTransferableProduct.builder()
                    .addIdentifier(ProductIdentifier.builder()
                            .setIdentifier(FieldWithMetaString.builder()
                                    .setValue(irsOtcModel.instrumentName)))
                    .setEconomicTerms(EconomicTerms.builder()
                            .setEffectiveDate(cdmDates.createAdjustableDate(irsOtcModel.fixedRateLegList.get(0).effectiveDate))
                            .setPayout(List.of(Payout.builder()
                                    .setInterestRatePayout(InterestRatePayout.builder()
                                            .setPriceQuantity(ResolvablePriceQuantity.builder()
                                                    .setQuantitySchedule(ReferenceWithMetaNonNegativeQuantitySchedule.builder()
                                                            .setReference(Reference.builder()
                                                                    .setScope("DOCUMENT")
                                                                    .setReference("quantity-1"))))
                                            .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder().setValue(DayCountFractionEnum.ACT_365_FIXED).build())
                                            .setCalculationPeriodDates(CalculationPeriodDates.builder()
                                                    .setEffectiveDate(AdjustableOrRelativeDate.builder()
                                                            .setAdjustableDate(AdjustableDate.builder()
                                                                    .setUnadjustedDate(cdmDates.createDate(irsOtcModel.floatingRateLegList.get(0).effectiveDate))
                                                                    .setDateAdjustments(BusinessDayAdjustments.builder()
                                                                            .setBusinessDayConvention(BusinessDayConventionEnum.NONE))))
                                                    .setTerminationDate(AdjustableOrRelativeDate.builder()
                                                            .setAdjustableDate(AdjustableDate.builder()
                                                                    .setUnadjustedDate(cdmDates.createDate(irsOtcModel.floatingRateLegList.get(0).maturityDate))
                                                                    .setDateAdjustments(BusinessDayAdjustments.builder()
                                                                            .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                                                            .setBusinessCenters(BusinessCenters.builder()
                                                                                    .setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
                                                                                            .setExternalReference("primaryBusinessCenters")
                                                                                            .build()))))
                                                    )
                                                    .setCalculationPeriodFrequency(CalculationPeriodFrequency.builder()
                                                            .setRollConvention(RollConventionEnum._3)
                                                            .setPeriodMultiplier(6)
                                                            .setPeriod(PeriodExtendedEnum.M))
                                                    .setCalculationPeriodDatesAdjustments(BusinessDayAdjustments.builder()
                                                            .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                                            .setBusinessCenters(BusinessCenters.builder()
                                                                    .setBusinessCentersReference(
                                                                            ReferenceWithMetaBusinessCenters.builder().setExternalReference("primaryBusinessCenters").build()))))

                                            .setPaymentDates(PaymentDates.builder()
                                                    .setPaymentFrequency(Frequency.builder()
                                                            .setPeriodMultiplier(3)
                                                            .setPeriod(PeriodExtendedEnum.M)))

                                            .setRateSpecification(RateSpecification.builder()
                                                    .setFloatingRateSpecification(FloatingRateSpecification.builder()
                                                            .setRateOptionValue(InterestRateIndex.builder()
                                                                    .setFloatingRateIndex(FloatingRateIndex.builder()
                                                                            .setFloatingRateIndexValue(FloatingRateIndexEnum.USD_SOFR_OIS_COMPOUND)
                                                                            .setIndexTenor(Period.builder()
                                                                                    .setPeriod(PeriodEnum.Y)
                                                                                    .setPeriodMultiplier(1))))))

                                            .setPayerReceiver(PayerReceiver.builder()
                                                    .setPayer(CounterpartyRoleEnum.PARTY_2)
                                                    .setReceiver(CounterpartyRoleEnum.PARTY_1)))))

                            .addPayout(Payout.builder()
                                    .setInterestRatePayout(InterestRatePayout.builder()
                                            .setPriceQuantity(ResolvablePriceQuantity.builder()
                                                    .setQuantitySchedule(ReferenceWithMetaNonNegativeQuantitySchedule.builder()
                                                            .setReference(Reference.builder()
                                                                    .setScope("DOCUMENT")
                                                                    .setReference("quantity-2"))))
                                            .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder().setValue(DayCountFractionEnum._30E_360).build())
                                            .setCalculationPeriodDates(CalculationPeriodDates.builder()
                                                    .setEffectiveDate(AdjustableOrRelativeDate.builder()
                                                            .setAdjustableDate(AdjustableDate.builder()
                                                                    .setUnadjustedDate(cdmDates.createDate(irsOtcModel.fixedRateLegList.get(0).effectiveDate))
                                                                    .setDateAdjustments(BusinessDayAdjustments.builder()
                                                                            .setBusinessDayConvention(BusinessDayConventionEnum.NONE))))
                                                    .setTerminationDate(AdjustableOrRelativeDate.builder()
                                                            .setAdjustableDate(AdjustableDate.builder()
                                                                    .setUnadjustedDate(cdmDates.createDate(irsOtcModel.fixedRateLegList.get(0).effectiveDate))
                                                                    .setDateAdjustments(BusinessDayAdjustments.builder()
                                                                            .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                                                            .setBusinessCenters(BusinessCenters.builder()
                                                                                    .setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
                                                                                            .setExternalReference("primaryBusinessCenters"))
                                                                                    .addBusinessCenter(
                                                                                            FieldWithMetaBusinessCenterEnum.builder().setValue(BusinessCenterEnum.EUTA).build())))))
                                                    .setCalculationPeriodFrequency(CalculationPeriodFrequency.builder()
                                                            .setRollConvention(RollConventionEnum._3)
                                                            .setPeriodMultiplier(3)
                                                            .setPeriod(PeriodExtendedEnum.M))
                                                    .setCalculationPeriodDatesAdjustments(BusinessDayAdjustments.builder()
                                                            .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                                            .setBusinessCenters(BusinessCenters.builder()
                                                                    .setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
                                                                            .setExternalReference("primaryBusinessCenters")))))
                                            .setPaymentDates(PaymentDates.builder()
                                                    .setPayRelativeTo(PayRelativeToEnum.CALCULATION_PERIOD_END_DATE)
                                                    .setPaymentFrequency(Frequency.builder()
                                                            .setPeriodMultiplier(1)
                                                            .setPeriod(PeriodExtendedEnum.Y)
                                                            .build())
                                                    .build())
                                            .setRateSpecification(RateSpecification.builder()
                                                    .setFixedRateSpecification(FixedRateSpecification.builder()
                                                            .setRateSchedule(RateSchedule.builder()
                                                                    .setPrice(ReferenceWithMetaPriceSchedule.builder()
                                                                            .setReference(Reference.builder()
                                                                                    .setScope("DOCUMENT")
                                                                                    .setReference("price-1"))
                                                                            .setValue(Price.builder()
                                                                                    .setValue(BigDecimal.valueOf(Double.parseDouble(irsOtcModel.fixedRateLegList.get(0).interestRate)))
                                                                                    .setUnit(UnitType.builder().setCurrencyValue(irsOtcModel.fixedRateLegList.get(0).currency))
                                                                                    .setPerUnitOf(UnitType.builder().setCurrencyValue(irsOtcModel.fixedRateLegList.get(0).currency))
                                                                                    .setPriceType(PriceTypeEnum.INTEREST_RATE))))))
                                            .setPayerReceiver(PayerReceiver.builder()
                                                    .setPayer(CounterpartyRoleEnum.PARTY_1)
                                                    .setReceiver(CounterpartyRoleEnum.PARTY_2))
                                            .build()))));

    return product;

  }

  public Product createFXOptionProduct(FXOptionModel fxOptionModel){

    CdmDates cdmDates = new CdmDates();

    Product product = Product.builder()
            .setNonTransferableProduct(NonTransferableProduct.builder()
                    .addIdentifier(ProductIdentifier.builder()
                            .setIdentifier(FieldWithMetaString.builder()
                                    .setValue(fxOptionModel.instrumentName)))
                    .setEconomicTerms(EconomicTerms.builder()
                            .setEffectiveDate(cdmDates.createAdjustableDate(fxOptionModel.effectiveDate))
                            .setTerminationDate(cdmDates.createAdjustableDate(fxOptionModel.expirationDate))
                            .setPayout(List.of(Payout.builder()
                                    .setOptionPayout(OptionPayout.builder()
                                            .setUnderlier(Underlier.builder()
                                                    .setProduct(Product.builder()
                                                            .setTransferableProduct(TransferableProduct.builder()
                                                                    .setCash(Cash.builder()
                                                                            .setIdentifier(List.of(AssetIdentifier.builder()
                                                                                    .setIdentifierType(AssetIdTypeEnum.CURRENCY_CODE)
                                                                                    .setIdentifierValue(fxOptionModel.underLyingAsset)))))))
                                            .setOptionType(OptionTypeEnum.CALL)
                                            .setExerciseTerms(ExerciseTerms.builder()
                                                    .setStyle(OptionExerciseStyleEnum.EUROPEAN)
                                                    .setExerciseDates(cdmDates.createAdjustableDates(fxOptionModel.expirationDate)))
                                            .setStrike(OptionStrike.builder()
                                                    .setStrikePrice(Price.builder()
                                                            .setValue(BigDecimal.valueOf(Double.parseDouble(fxOptionModel.strike))))))))))
            .build();

    return product;

  }

  public Product createFXCashProduct(FXCashModel fxCashModel){

    CdmDates cdmDates = new CdmDates();
    AdjustableOrRelativeDate settlementDate = cdmDates.createAdjustableDate(fxCashModel.valueDate);

    Product product = Product.builder()
            .setNonTransferableProduct(NonTransferableProduct.builder()
                    .addIdentifier(ProductIdentifier.builder()
                            .setIdentifier(FieldWithMetaString.builder()
                                    .setValue(fxCashModel.instrumentName))
                            .setSource(ProductIdTypeEnum.NAME.NAME))
                    .setEconomicTerms(EconomicTerms.builder()
                            .setPayout(List.of(Payout.builder()
                                    .setSettlementPayout(SettlementPayout.builder()
                                            .setUnderlier(Underlier.builder()
                                                    .setProduct(Product.builder()
                                                            .setTransferableProduct(TransferableProduct.builder()
                                                                    .setCash(Cash.builder()
                                                                            .setIdentifier(List.of(AssetIdentifier.builder()
                                                                                    .setIdentifierType(AssetIdTypeEnum.CURRENCY_CODE)
                                                                                    .setIdentifierValue(fxCashModel.currency2)))))))
                                            .setPayerReceiver(PayerReceiver.builder()
                                                    .setPayer(CounterpartyRoleEnum.PARTY_2)
                                                    .setReceiver(CounterpartyRoleEnum.PARTY_1)))))
                            .setEffectiveDate(settlementDate)
                            .setTerminationDate(settlementDate)))
            .build();

    return product;
  }


  public Product createBondProduct(BondModel bondModel){

    CdmDates cdmDates = new CdmDates();
    DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");


    ZonedDateTime zonedMaturityDate = ZonedDateTime.parse(bondModel.maturityDate, formatter);
    Date terminationDate = Date.of(zonedMaturityDate.getYear(), zonedMaturityDate.getMonthValue(), zonedMaturityDate.getDayOfMonth());

    ZonedDateTime zonedEffectiveDate = ZonedDateTime.parse(bondModel.effectiveDate, formatter);
    Date effectiveDate = Date.of(zonedEffectiveDate.getYear(), zonedEffectiveDate.getMonthValue(),zonedEffectiveDate.getDayOfMonth());

    Product product = Product.builder()
            .setNonTransferableProduct(NonTransferableProduct.builder()
                    .setIdentifier(List.of(ProductIdentifier.builder()
                            .setIdentifier(FieldWithMetaString.builder()
                                    .setValue(bondModel.issuer))
                            .setSource(ProductIdTypeEnum.NAME.NAME)))
                    .addIdentifier(ProductIdentifier.builder()
                            .setSource(ProductIdTypeEnum.ISIN)
                            .setIdentifierValue(bondModel.getIsin()))
                    .addIdentifier(ProductIdentifier.builder()
                            .setIdentifier(FieldWithMetaString.builder()
                                    .setValue(bondModel.instrumentName))
                            .setSource(ProductIdTypeEnum.NAME.NAME))

                    .setEconomicTerms(EconomicTerms.builder()
                            .setTerminationDate(AdjustableOrRelativeDate.builder()
                                    .setAdjustableDate(AdjustableDate.builder()
                                            .setAdjustedDate(FieldWithMetaDate.builder()
                                                    .setValue(terminationDate))))
                            .setEffectiveDate(AdjustableOrRelativeDate.builder()
                                    .setAdjustableDate(AdjustableDate.builder()
                                            .setAdjustedDate(FieldWithMetaDate.builder()
                                                    .setValue(effectiveDate))))

                            .addPayout(Payout.builder()
                                    .setSettlementPayout(SettlementPayout.builder()
                                            .setPayerReceiver(PayerReceiver.builder()
                                                    .setPayer(CounterpartyRoleEnum.PARTY_1)
                                                    .setReceiver(CounterpartyRoleEnum.PARTY_2))
                                            .setPriceQuantity(ResolvablePriceQuantity.builder()
                                                    .setQuantitySchedule(ReferenceWithMetaNonNegativeQuantitySchedule.builder()
                                                            .setReference(Reference.builder()
                                                                    .setScope("DOCUMENT")
                                                                    .setReference("quantity-1")))
                                                    .setPriceSchedule(List.of(ReferenceWithMetaPriceSchedule.builder()
                                                                    .setReference(Reference.builder()
                                                                            .setScope("DOCUMENT")
                                                                            .setReference("price-1"))))
                                                    .setMeta(MetaFields.builder()
                                                            .setGlobalKey("0")))
                                            .setUnderlier(Underlier.builder()
                                                    .setProduct(Product.builder()
                                                            .setTransferableProduct(TransferableProduct.builder()
                                                                    .setInstrument(Instrument.builder()
                                                                            .setSecurity(Security.builder()
                                                                                    .setIdentifier(List.of(AssetIdentifier.builder()
                                                                                            .setIdentifierValue(bondModel.getIsin())
                                                                                            .setIdentifierType(AssetIdTypeEnum.ISIN)))))
                                                                    .setEconomicTerms(EconomicTerms.builder()
                                                                            .setPayout(List.of(Payout.builder()
                                                                                    .setInterestRatePayout(InterestRatePayout.builder()
                                                                                            .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder()
                                                                                                    .setValue(DayCountFractionEnum._30_360))
                                                                                            .setPaymentDates(PaymentDates.builder()
                                                                                                    .setPaymentFrequency(Frequency.builder()
                                                                                                            .setPeriod(PeriodExtendedEnum.M)
                                                                                                            .setPeriodMultiplier(6)
                                                                                                            .build())
                                                                                                    .build())
                                                                                            .setRateSpecification(RateSpecification.builder()
                                                                                                    .setFixedRateSpecification(FixedRateSpecification.builder()
                                                                                                            .setRateSchedule(RateSchedule.builder()
                                                                                                                    .setPriceValue(PriceSchedule.builder()
                                                                                                                            .setPriceExpression(PriceExpressionEnum.PAR_VALUE_FRACTION))
                                                                                                                    .setPrice(ReferenceWithMetaPriceSchedule.builder()
                                                                                                                            .setValue(PriceSchedule.builder()
                                                                                                                                    .setValue(BigDecimal.valueOf(Double.parseDouble(bondModel.couponRate)
                                                                                                                                    )))))))))))))))
                                    .setMeta(MetaFields.builder()
                                            .setGlobalKey("a314fba4"))
                                    .setMeta(MetaFields.builder()
                                            .setGlobalKey("a314fba4")))))

            .build();

    return product;

  }

  public Product createTIPSProduct(BondModel bondModel){

    CdmDates cdmDates = new CdmDates();
    DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");


    ZonedDateTime zonedMaturityDate = ZonedDateTime.parse(bondModel.maturityDate, formatter);
    Date terminationDate = Date.of(zonedMaturityDate.getYear(), zonedMaturityDate.getMonthValue(), zonedMaturityDate.getDayOfMonth());

    ZonedDateTime zonedEffectiveDate = ZonedDateTime.parse(bondModel.effectiveDate, formatter);
    Date effectiveDate = Date.of(zonedEffectiveDate.getYear(), zonedEffectiveDate.getMonthValue(),zonedEffectiveDate.getDayOfMonth());

    Product product = Product.builder()
            .setNonTransferableProduct(NonTransferableProduct.builder()
                    .setIdentifier(List.of(ProductIdentifier.builder()
                            .setIdentifier(FieldWithMetaString.builder()
                                    .setValue(bondModel.issuer))
                            .setSource(ProductIdTypeEnum.NAME.NAME)))
                    .addIdentifier(ProductIdentifier.builder()
                            .setSource(ProductIdTypeEnum.ISIN)
                            .setIdentifierValue(bondModel.getIsin()))
                    .addIdentifier(ProductIdentifier.builder()
                            .setIdentifier(FieldWithMetaString.builder()
                                    .setValue(bondModel.instrumentName))
                            .setSource(ProductIdTypeEnum.NAME.NAME))

                    .setEconomicTerms(EconomicTerms.builder()
                            .setTerminationDate(AdjustableOrRelativeDate.builder()
                                    .setAdjustableDate(AdjustableDate.builder()
                                            .setAdjustedDate(FieldWithMetaDate.builder()
                                                    .setValue(terminationDate))))
                            .setEffectiveDate(AdjustableOrRelativeDate.builder()
                                    .setAdjustableDate(AdjustableDate.builder()
                                            .setAdjustedDate(FieldWithMetaDate.builder()
                                                    .setValue(effectiveDate))))

                            .addPayout(Payout.builder()
                                    .setSettlementPayout(SettlementPayout.builder()
                                            .setPayerReceiver(PayerReceiver.builder()
                                                    .setPayer(CounterpartyRoleEnum.PARTY_1)
                                                    .setReceiver(CounterpartyRoleEnum.PARTY_2))
                                            .setPriceQuantity(ResolvablePriceQuantity.builder()
                                                    .setQuantitySchedule(ReferenceWithMetaNonNegativeQuantitySchedule.builder()
                                                            .setReference(Reference.builder()
                                                                    .setScope("DOCUMENT")
                                                                    .setReference("quantity-1")))
                                                    .setPriceSchedule(List.of(ReferenceWithMetaPriceSchedule.builder()
                                                            .setReference(Reference.builder()
                                                                    .setScope("DOCUMENT")
                                                                    .setReference("price-1"))))
                                                    .setMeta(MetaFields.builder()
                                                            .setGlobalKey("0")))
                                            .setUnderlier(Underlier.builder()
                                                    .setProduct(Product.builder()
                                                            .setTransferableProduct(TransferableProduct.builder()
                                                                    .setInstrument(Instrument.builder()
                                                                            .setSecurity(Security.builder()
                                                                                    .setIdentifier(List.of(AssetIdentifier.builder()
                                                                                            .setIdentifierValue(bondModel.getIsin())
                                                                                            .setIdentifierType(AssetIdTypeEnum.ISIN)))))
                                                                    .setEconomicTerms(EconomicTerms.builder()
                                                                            .setPayout(List.of(Payout.builder()
                                                                                    .setInterestRatePayout(InterestRatePayout.builder()
                                                                                            .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder()
                                                                                                    .setValue(DayCountFractionEnum._30_360))
                                                                                            .setPaymentDates(PaymentDates.builder()
                                                                                                    .setPaymentFrequency(Frequency.builder()
                                                                                                            .setPeriod(PeriodExtendedEnum.M)
                                                                                                            .setPeriodMultiplier(6)
                                                                                                            .build())
                                                                                                    .build())
                                                                                            .setRateSpecification(RateSpecification.builder()
                                                                                                    .setFixedRateSpecification(FixedRateSpecification.builder()
                                                                                                            .setRateSchedule(RateSchedule.builder()
                                                                                                                    .setPriceValue(PriceSchedule.builder()
                                                                                                                            .setPriceExpression(PriceExpressionEnum.PAR_VALUE_FRACTION))
                                                                                                                    .setPrice(ReferenceWithMetaPriceSchedule.builder()
                                                                                                                            .setValue(PriceSchedule.builder()
                                                                                                                                    .setValue(BigDecimal.valueOf(Double.parseDouble(bondModel.couponRate)
                                                                                                                                    )))))))
                                                                                            .setPriceQuantity(ResolvablePriceQuantity.builder()
                                                                                                    .setQuantitySchedule(ReferenceWithMetaNonNegativeQuantitySchedule.builder()
                                                                                                            .setValue(NonNegativeQuantitySchedule.builder()
                                                                                                                    .setDatedValue(List.of(
                                                                                                                            DatedValue.builder()
                                                                                                                                    .setMeta(MetaFields.builder()
                                                                                                                                            .setExternalKey("adjusted-principle-1")),
                                                                                                                            DatedValue.builder()
                                                                                                                                    .setMeta(MetaFields.builder()
                                                                                                                                            .setExternalKey("adjusted-principle-2")),
                                                                                                                            DatedValue.builder()
                                                                                                                                    .setMeta(MetaFields.builder()
                                                                                                                                            .setExternalKey("adjusted-principle-2"))))))))

                                                                    .setMeta(MetaFields.builder()
                                                                            .setGlobalKey("a314fba4"))
                                                                    .setMeta(MetaFields.builder()
                                                                            .setGlobalKey("a314fba4"))))))))
                                            .setPrincipalPayment(PrincipalPayments.builder()
                                                    .setFinalPayment(true)
                                                    .setPrincipalPaymentSchedule(PrincipalPaymentSchedule.builder()
                                                            .setFinalPrincipalPayment(PrincipalPayment.builder()
                                                                    .setMeta(MetaFields.builder()
                                                                            .setGlobalKey("final-principal-payment")))))))))

            .build();

    return product;

  }

  public Product createBondFutureProduct(BondFutureModel bondFutureModel){

    CdmDates cdmDates = new CdmDates();

    DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
    ZonedDateTime maturityDate = ZonedDateTime.parse(bondFutureModel.maturityDate, formatter);
    Date terminationDate = Date.of(maturityDate.getYear(), maturityDate.getMonthValue(), maturityDate.getDayOfMonth());

    ZonedDateTime zonedSettlementDate = ZonedDateTime.parse(bondFutureModel.tradeDate, formatter);
    Date settlementDate = Date.of(zonedSettlementDate.getYear(), zonedSettlementDate.getMonthValue(), zonedSettlementDate.getDayOfMonth());

    Product product = Product.builder()
            .setNonTransferableProduct(NonTransferableProduct.builder()
                    .setIdentifier(List.of(ProductIdentifier.builder()
                            .setIdentifierValue(bondFutureModel.instrumentName)
                            .setSource(ProductIdTypeEnum.NAME)))
                    .setEconomicTerms(EconomicTerms.builder()
                            .setTerminationDate(AdjustableOrRelativeDate.builder()
                                    .setAdjustableDate(AdjustableDate.builder()
                                            .setAdjustedDate(FieldWithMetaDate.builder()
                                                    .setValue(terminationDate))))
                            .setEffectiveDate(AdjustableOrRelativeDate.builder()
                                    .setAdjustableDate(AdjustableDate.builder()
                                            .setAdjustedDate(FieldWithMetaDate.builder()
                                                    .setValue(settlementDate))))
                            .setPayout(List.of(Payout.builder()
                                    .setAssetPayout(AssetPayout.builder()
                                            .setUnderlier(Asset.builder()
                                                    .setInstrument(Instrument.builder()
                                                            .setSecurity(Security.builder()
                                                                    .setDebtType(DebtType.builder()
                                                                            .setDebtClass(DebtClassEnum.VANILLA)
                                                                            .build())
                                                                    .setIdentifier(List.of(AssetIdentifier.builder()
                                                                            .setIdentifierType(AssetIdTypeEnum.ISIN)
                                                                            .setIdentifierValue(bondFutureModel.underlyingIsin)))))))))

                            .addPayout(Payout.builder()
                                    .setSettlementPayout(SettlementPayout.builder()
                                            .setPayerReceiver(PayerReceiver.builder()
                                                    .setPayer(CounterpartyRoleEnum.PARTY_1)
                                                    .setReceiver(CounterpartyRoleEnum.PARTY_2))
                                            .setSettlementTerms(SettlementTerms.builder()
                                                    .setSettlementDate(SettlementDate.builder()
                                                            .setAdjustableOrRelativeDate(AdjustableOrAdjustedOrRelativeDate.builder()
                                                                    .setAdjustedDate(FieldWithMetaDate.builder()
                                                                            .setValue(settlementDate)))))))))
            .build();

    return product;

  }
}
