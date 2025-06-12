package com.finxis.trade;

import cdm.base.datetime.AdjustableOrRelativeDate;
import cdm.base.datetime.TimeZone;
import cdm.base.datetime.metafields.FieldWithMetaTimeZone;
import cdm.base.math.ArithmeticOperationEnum;
import cdm.base.math.FinancialUnitEnum;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import cdm.base.staticdata.asset.common.*;
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.identifier.TradeIdentifierTypeEnum;
import cdm.base.staticdata.party.*;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.ExecutionDetails;
import cdm.event.common.ExecutionInstruction;
import cdm.event.common.Trade;
import cdm.event.common.TradeIdentifier;
import cdm.observable.asset.*;
import cdm.observable.asset.metafields.FieldWithMetaObservable;
import cdm.observable.asset.metafields.FieldWithMetaPriceSchedule;
import cdm.product.template.Product;
import cdm.product.template.TradeLot;
import com.finxis.CdmBusinessEvent;
import com.finxis.models.BondFutureModel;
import com.finxis.models.BondModel;
import com.finxis.models.TradeModel;
import com.finxis.util.CdmDates;
import com.finxis.CdmBusinessEvent.*;
import com.rosetta.model.lib.meta.Key;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CreateTrade {

  public ExecutionInstruction createIrsOtcTrade(Product irsOtcProduct, TradeModel irsOctTradeModel) throws IOException {
    CdmDates cdmDates = new CdmDates();
    CdmBusinessEvent cdmBusinessEvent = new CdmBusinessEvent();

    AdjustableOrRelativeDate settlementDate = cdmDates.createAdjustableDate(irsOctTradeModel.settlementDate);
    Date tradeDate = cdmDates.createDate(irsOctTradeModel.tradeDate);

    Party buyerParty = Party.builder()
            .setPartyId(List.of(PartyIdentifier.builder()
                    .setIdentifierValue("ClientId"))).build();


    Party sellerParty = Party.builder()
            .setPartyId(List.of(PartyIdentifier.builder()
                    .setIdentifierValue("DealerId"))).build();

    Party traderParty = Party.builder()
            .setPartyId(List.of(PartyIdentifier.builder()
                    .setIdentifierValue("Trader"))).build();

    List<Party> partyList = List.of(buyerParty, sellerParty, traderParty);

    PartyRole buyer = PartyRole.builder()
            .setRole(PartyRoleEnum.BUYER)
            .setPartyReference(ReferenceWithMetaParty.builder()
                    .setValue(buyerParty))
            .build();

    PartyRole seller = PartyRole.builder()
            .setRole(PartyRoleEnum.SELLER)
            .setPartyReference(ReferenceWithMetaParty.builder()
                    .setValue(sellerParty))
            .build();

    PartyRole trader = PartyRole.builder()
            .setRole(PartyRoleEnum.BOOKING_PARTY)
            .setPartyReference(ReferenceWithMetaParty.builder()
                    .setValue(traderParty))
            .build();

    List<PartyRole> partyRoleList = List.of(buyer, seller, trader);

    Trade trade = Trade.builder()
            .setProduct(irsOtcProduct.getNonTransferableProduct())
            .setTradeDate(FieldWithMetaDate.builder()
                    .setValue(tradeDate))
            .setTradeIdentifier(List.of(TradeIdentifier.builder()
                    .setAssignedIdentifier(List.of(AssignedIdentifier.builder()
                            .setIdentifierValue(irsOctTradeModel.tradeId)))))
            .setCounterparty(List.of(Counterparty.builder()
                    .setPartyReference(ReferenceWithMetaParty.builder()
                            .setExternalReference("Client"))
                    .setRole(CounterpartyRoleEnum.PARTY_1)))
            .addCounterparty(Counterparty.builder()
                    .setPartyReference(ReferenceWithMetaParty.builder()
                            .setExternalReference("Dealer"))
                    .setRole(CounterpartyRoleEnum.PARTY_2))
            .setParty(partyList)
            .setPartyRole(partyRoleList)
            .setTradeLot(List.of(TradeLot.builder()
                    .setPriceQuantity(List.of(PriceQuantity.builder()
                            .setPrice(List.of(FieldWithMetaPriceSchedule.builder()
                                    .setValue(PriceSchedule.builder()
                                            .setValue(BigDecimal.valueOf(Double.parseDouble(irsOctTradeModel.price)))
                                            .setUnit(UnitType.builder()
                                                    .setCurrencyValue(irsOctTradeModel.priceCurrency))
                                            .setPerUnitOf(UnitType.builder()
                                                    .setCurrencyValue(irsOctTradeModel.priceCurrency))
                                            .setPriceType(PriceTypeEnum.ASSET_PRICE)
                                            .setPriceExpression(PriceExpressionEnum.ABSOLUTE_TERMS)
                                            .setComposite(PriceComposite.builder()
                                                    .setBaseValue(BigDecimal.valueOf(Double.parseDouble(irsOctTradeModel.price)))
                                                    .setOperand(BigDecimal.valueOf(Double.parseDouble(".0213")))
                                                    .setArithmeticOperator(ArithmeticOperationEnum.ADD)
                                                    .setOperandType(PriceOperandEnum.ACCRUED_INTEREST)))))
                            .setQuantity(List.of(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                    .setValue(NonNegativeQuantitySchedule.builder()
                                            .setValue(BigDecimal.valueOf(Double.parseDouble(irsOctTradeModel.quantity)))
                                            .setUnit(UnitType.builder()
                                                    .setCurrencyValue(irsOctTradeModel.priceCurrency)))))
                            .setObservable(FieldWithMetaObservable.builder()
                                    .setValue(Observable.builder()
                                            .setAsset(Asset.builder()
                                                    .setCash(Cash.builder()
                                                            .setIdentifier(List.of(AssetIdentifier.builder()
                                                                    .setIdentifierType(AssetIdTypeEnum.CURRENCY_CODE)
                                                                    .setIdentifierValue(irsOctTradeModel.priceCurrency)))))))))))

            .build();

    ExecutionInstruction executionInstruction = ExecutionInstruction.builder()
            .setProduct(irsOtcProduct.getNonTransferableProduct())
            .addPriceQuantity(trade.getTradeLot().get(0).getPriceQuantity())
            .addCounterparty(trade.getCounterparty())
            .addParties(trade.getParty())
            .addPartyRoles(trade.getPartyRole())
            .setExecutionDetails(trade.getExecutionDetails())
            .setTradeDate(trade.getTradeDate())
            .addTradeIdentifier(trade.getTradeIdentifier())
            .build();


    return executionInstruction;

  }

  public  ExecutionInstruction createFXOptionTrade(Product fxOptionProduct, TradeModel fxOptionTradeModel) throws IOException {

    CdmDates cdmDates = new CdmDates();
    CdmBusinessEvent cdmBusinessEvent = new CdmBusinessEvent();

    AdjustableOrRelativeDate settlementDate = cdmDates.createAdjustableDate(fxOptionTradeModel.settlementDate);
    Date tradeDate = cdmDates.createDate(fxOptionTradeModel.tradeDate);

    Party buyerParty = Party.builder()
            .setPartyId(List.of(PartyIdentifier.builder()
                    .setIdentifierValue("ClientId"))).build();


    Party sellerParty = Party.builder()
            .setPartyId(List.of(PartyIdentifier.builder()
                    .setIdentifierValue("DealerId"))).build();

    Party traderParty = Party.builder()
            .setPartyId(List.of(PartyIdentifier.builder()
                    .setIdentifierValue("Trader"))).build();

    List<Party> partyList = List.of(buyerParty, sellerParty, traderParty);

    PartyRole buyer = PartyRole.builder()
            .setRole(PartyRoleEnum.BUYER)
            .setPartyReference(ReferenceWithMetaParty.builder()
                    .setValue(buyerParty))
            .build();

    PartyRole seller = PartyRole.builder()
            .setRole(PartyRoleEnum.SELLER)
            .setPartyReference(ReferenceWithMetaParty.builder()
                    .setValue(sellerParty))
            .build();

    PartyRole trader = PartyRole.builder()
            .setRole(PartyRoleEnum.BOOKING_PARTY)
            .setPartyReference(ReferenceWithMetaParty.builder()
                    .setValue(traderParty))
            .build();

    List<PartyRole> partyRoleList = List.of(buyer, seller, trader);

    Trade trade = Trade.builder()
            .setProduct(fxOptionProduct.getNonTransferableProduct())
            .setTradeDate(FieldWithMetaDate.builder()
                    .setValue(tradeDate))
            .setTradeIdentifier(List.of(TradeIdentifier.builder()
                    .setAssignedIdentifier(List.of(AssignedIdentifier.builder()
                            .setIdentifierValue(fxOptionTradeModel.tradeId)))))
            .setCounterparty(List.of(Counterparty.builder()
                    .setPartyReference(ReferenceWithMetaParty.builder()
                            .setExternalReference("Client"))
                    .setRole(CounterpartyRoleEnum.PARTY_1)))
            .addCounterparty(Counterparty.builder()
                    .setPartyReference(ReferenceWithMetaParty.builder()
                            .setExternalReference("Dealer"))
                    .setRole(CounterpartyRoleEnum.PARTY_2))
            .setParty(partyList)
            .setPartyRole(partyRoleList)
            .setTradeLot(List.of(TradeLot.builder()
                    .setPriceQuantity(List.of(PriceQuantity.builder()
                            .setPrice(List.of(FieldWithMetaPriceSchedule.builder()
                                    .setValue(PriceSchedule.builder()
                                            .setValue(BigDecimal.valueOf(Double.parseDouble(fxOptionTradeModel.price)))
                                            .setUnit(UnitType.builder()
                                                    .setCurrencyValue(fxOptionTradeModel.priceCurrency))
                                            .setPerUnitOf(UnitType.builder()
                                                    .setCurrencyValue(fxOptionTradeModel.priceCurrency))
                                            .setPriceType(PriceTypeEnum.ASSET_PRICE)
                                            .setPriceExpression(PriceExpressionEnum.ABSOLUTE_TERMS)
                                            .setComposite(PriceComposite.builder()
                                                    .setBaseValue(BigDecimal.valueOf(Double.parseDouble(fxOptionTradeModel.price)))
                                                    .setOperand(BigDecimal.valueOf(Double.parseDouble(".0213")))
                                                    .setArithmeticOperator(ArithmeticOperationEnum.ADD)
                                                    .setOperandType(PriceOperandEnum.ACCRUED_INTEREST)))))
                            .setQuantity(List.of(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                    .setValue(NonNegativeQuantitySchedule.builder()
                                            .setValue(BigDecimal.valueOf(Double.parseDouble(fxOptionTradeModel.quantity)))
                                            .setUnit(UnitType.builder()
                                                    .setCurrencyValue(fxOptionTradeModel.priceCurrency)))))
                            .setObservable(FieldWithMetaObservable.builder()
                                    .setValue(Observable.builder()
                                            .setAsset(Asset.builder()
                                                    .setCash(Cash.builder()
                                                            .setIdentifier(List.of(AssetIdentifier.builder()
                                                                    .setIdentifierType(AssetIdTypeEnum.CURRENCY_CODE)
                                                                    .setIdentifierValue(fxOptionTradeModel.priceCurrency)))))))))))

            .build();

    ExecutionInstruction executionInstruction = ExecutionInstruction.builder()
            .setProduct(fxOptionProduct.getNonTransferableProduct())
            .addPriceQuantity(trade.getTradeLot().get(0).getPriceQuantity())
            .addCounterparty(trade.getCounterparty())
            .addParties(trade.getParty())
            .addPartyRoles(trade.getPartyRole())
            .setExecutionDetails(trade.getExecutionDetails())
            .setTradeDate(trade.getTradeDate())
            .addTradeIdentifier(trade.getTradeIdentifier())
            .build();

    return executionInstruction;
  }

  public  ExecutionInstruction createFXCashTrade(Product fxCashProduct, TradeModel fxTradeModel) throws IOException {

    CdmDates cdmDates = new CdmDates();
    CdmBusinessEvent cdmBusinessEvent = new CdmBusinessEvent();

    AdjustableOrRelativeDate settlementDate = cdmDates.createAdjustableDate(fxTradeModel.settlementDate);
    Date tradeDate = cdmDates.createDate(fxTradeModel.tradeDate);

    Party buyerParty = Party.builder()
            .setPartyId(List.of(PartyIdentifier.builder()
                    .setIdentifierValue("ClientId"))).build();


    Party sellerParty = Party.builder()
            .setPartyId(List.of(PartyIdentifier.builder()
                    .setIdentifierValue("DealerId"))).build();

    Party traderParty = Party.builder()
            .setPartyId(List.of(PartyIdentifier.builder()
                    .setIdentifierValue("Trader"))).build();

    List<Party> partyList = List.of(buyerParty, sellerParty, traderParty);

    PartyRole buyer = PartyRole.builder()
            .setRole(PartyRoleEnum.BUYER)
            .setPartyReference(ReferenceWithMetaParty.builder()
                    .setValue(buyerParty))
            .build();

    PartyRole seller = PartyRole.builder()
            .setRole(PartyRoleEnum.SELLER)
            .setPartyReference(ReferenceWithMetaParty.builder()
                    .setValue(sellerParty))
            .build();

    PartyRole trader = PartyRole.builder()
            .setRole(PartyRoleEnum.BOOKING_PARTY)
            .setPartyReference(ReferenceWithMetaParty.builder()
                    .setValue(traderParty))
            .build();

    List<PartyRole> partyRoleList = List.of(buyer, seller, trader);

    Trade trade = Trade.builder()
            .setProduct(fxCashProduct.getNonTransferableProduct())
            .setTradeDate(FieldWithMetaDate.builder()
                    .setValue(tradeDate))
            .setTradeIdentifier(List.of(TradeIdentifier.builder()
                    .setAssignedIdentifier(List.of(AssignedIdentifier.builder()
                            .setIdentifierValue(fxTradeModel.tradeId)))))
            .setCounterparty(List.of(Counterparty.builder()
                    .setPartyReference(ReferenceWithMetaParty.builder()
                            .setExternalReference("Client"))
                    .setRole(CounterpartyRoleEnum.PARTY_1)))
            .addCounterparty(Counterparty.builder()
                    .setPartyReference(ReferenceWithMetaParty.builder()
                            .setExternalReference("Dealer"))
                    .setRole(CounterpartyRoleEnum.PARTY_2))
            .setParty(partyList)
            .setPartyRole(partyRoleList)
            .setTradeLot(List.of(TradeLot.builder()
                    .setPriceQuantity(List.of(PriceQuantity.builder()
                            .setPrice(List.of(FieldWithMetaPriceSchedule.builder()
                                    .setValue(PriceSchedule.builder()
                                            .setValue(BigDecimal.valueOf(Double.parseDouble(fxTradeModel.price)))
                                            .setUnit(UnitType.builder()
                                                    .setCurrencyValue(fxTradeModel.priceCurrency))
                                            .setPerUnitOf(UnitType.builder()
                                                    .setCurrencyValue(fxTradeModel.priceCurrency))
                                            .setPriceType(PriceTypeEnum.ASSET_PRICE)
                                            .setPriceExpression(PriceExpressionEnum.ABSOLUTE_TERMS)
                                            .setComposite(PriceComposite.builder()
                                                    .setBaseValue(BigDecimal.valueOf(Double.parseDouble(fxTradeModel.price)))
                                                    .setOperand(BigDecimal.valueOf(Double.parseDouble(".0213")))
                                                    .setArithmeticOperator(ArithmeticOperationEnum.ADD)
                                                    .setOperandType(PriceOperandEnum.ACCRUED_INTEREST)))))
                            .setMeta(MetaFields.builder()
                                    .setKey(List.of(Key.builder()
                                            .setScope("DOCUMENT")
                                            .setKeyValue("price-1"))))
                            .setQuantity(List.of(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                    .setValue(NonNegativeQuantitySchedule.builder()
                                            .setValue(BigDecimal.valueOf(Double.parseDouble(fxTradeModel.quantity)))
                                            .setUnit(UnitType.builder()
                                                    .setCurrencyValue(fxTradeModel.priceCurrency)))))
                                    .setMeta(MetaFields.builder()
                                            .setKey(List.of(Key.builder()
                                                            .setScope("DOCUMENT")
                                                            .setKeyValue("quantity-1"))))
                            .setObservable(FieldWithMetaObservable.builder()
                                    .setValue(Observable.builder()
                                            .setAsset(Asset.builder()
                                                    .setCash(Cash.builder()
                                                            .setIdentifier(List.of(AssetIdentifier.builder()
                                                                    .setIdentifierType(AssetIdTypeEnum.CURRENCY_CODE)
                                                                    .setIdentifierValue(fxTradeModel.priceCurrency)))))))
                            .setMeta(MetaFields.builder()
                                    .setKey(List.of(Key.builder()
                                            .setScope("DOCUMENT")
                                            .setKeyValue("observerable-1"))))
                            .setMeta(MetaFields.builder()
                                    .setGlobalKey("a314fba4"))))))

            .build();

    ExecutionInstruction executionInstruction = ExecutionInstruction.builder()
            .setProduct(fxCashProduct.getNonTransferableProduct())
            .addPriceQuantity(trade.getTradeLot().get(0).getPriceQuantity())
            .addCounterparty(trade.getCounterparty())
            .addParties(trade.getParty())
            .addPartyRoles(trade.getPartyRole())
            .setExecutionDetails(trade.getExecutionDetails())
            .setTradeDate(trade.getTradeDate())
            .addTradeIdentifier(trade.getTradeIdentifier())
            .build();


    return executionInstruction;


  }

  public  ExecutionInstruction createBondTrade(Product bond, BondModel bondModel) throws IOException {

    String tradeDateTime = "2025-02-27T15:38:16.000+00:00";
    String tradeTime = "153816";
    String price = "99.80000000";
    String nominalQuantity = "500000.00000000";
    String marketid = "BCF";
    String platform = "Bloomberg";
    String commission = "20.45";
    String settlementDate = "2025-03-03T00:00:00.000+00:00";

    CdmDates cdmDates = new CdmDates();
    CdmBusinessEvent cdmBusinessEvent = new CdmBusinessEvent();

    DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
    ZonedDateTime tradeDate = ZonedDateTime.parse(tradeDateTime, formatter);
    Date tradeDateStr= Date.of(tradeDate.getYear(), tradeDate.getMonthValue(), tradeDate.getDayOfMonth());

    Party buyerParty = Party.builder()
            .setPartyId(List.of(PartyIdentifier.builder()
                    .setIdentifierValue("ClientId"))).build();


    Party sellerParty = Party.builder()
            .setPartyId(List.of(PartyIdentifier.builder()
                    .setIdentifierValue("DealerId"))).build();

    Party traderParty = Party.builder()
            .setPartyId(List.of(PartyIdentifier.builder()
                    .setIdentifierValue("Trader"))).build();

    List<Party> partyList = List.of(buyerParty, sellerParty, traderParty);

    PartyRole buyer = PartyRole.builder()
            .setRole(PartyRoleEnum.BUYER)
            .setPartyReference(ReferenceWithMetaParty.builder()
                    .setValue(buyerParty))
            .build();

    PartyRole seller = PartyRole.builder()
            .setRole(PartyRoleEnum.SELLER)
            .setPartyReference(ReferenceWithMetaParty.builder()
                    .setValue(sellerParty))
            .build();

    PartyRole trader = PartyRole.builder()
            .setRole(PartyRoleEnum.BOOKING_PARTY)
            .setPartyReference(ReferenceWithMetaParty.builder()
                    .setValue(traderParty))
            .build();

    List<PartyRole> partyRoleList = List.of(buyer, seller, trader);


    Trade trade = Trade.builder()
            .setProduct(bond.getNonTransferableProduct())
            .setTradeDate(FieldWithMetaDate.builder()
                    .setValue(tradeDateStr))
            .setTradeTime(FieldWithMetaTimeZone.builder()
                    .setValue(TimeZone.builder()
                            .setLocation(FieldWithMetaString.builder()
                                    .setValue("UTC"))))
            .setTradeIdentifier(List.of(TradeIdentifier.builder()
                    .setIdentifierType(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER)
                    .setAssignedIdentifier(List.of(AssignedIdentifier.builder()
                            .setIdentifierValue("UTI123")))))
            .setTradeLot(List.of(TradeLot.builder()
                        .setLotIdentifier(List.of(Identifier.builder()
                                    .setAssignedIdentifier(List.of(AssignedIdentifier.builder()
                                            .setIdentifierValue("LOT-0")))))
                        .setPriceQuantity(List.of(PriceQuantity.builder()
                            .setPrice(List.of(FieldWithMetaPriceSchedule.builder()
                                    .setValue(PriceSchedule.builder()
                                            .setValue(BigDecimal.valueOf(Double.parseDouble(price)))
                                            .setUnit(UnitType.builder()
                                                    .setCurrencyValue(bondModel.currency))
                                            .setPerUnitOf(UnitType.builder()
                                                    .setCurrencyValue(bondModel.currency))
                                            .setPriceType(PriceTypeEnum.ASSET_PRICE)
                                            .setPriceExpression(PriceExpressionEnum.PAR_VALUE_FRACTION)
                                            .setComposite(PriceComposite.builder()
                                                    .setBaseValue(BigDecimal.valueOf(Double.parseDouble(price)))
                                                    .setOperand(BigDecimal.valueOf(Double.parseDouble(".0213")))
                                                    .setArithmeticOperator(ArithmeticOperationEnum.ADD)
                                                    .setOperandType(PriceOperandEnum.ACCRUED_INTEREST)))))
                            .setQuantity(List.of(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                    .setValue(NonNegativeQuantitySchedule.builder()
                                            .setValue(BigDecimal.valueOf(Double.parseDouble(nominalQuantity)))
                                            .setUnit(UnitType.builder()
                                                    .setCurrencyValue(bondModel.currency)))))
                            .setObservable(FieldWithMetaObservable.builder()
                                    .setValue(Observable.builder()
                                            .setAsset(Asset.builder()
                                                    .setInstrument(Instrument.builder()
                                                            .setSecurity(Security.builder()
                                                                    .setIdentifier(List.of(AssetIdentifier.builder()
                                                                            .setIdentifierType(AssetIdTypeEnum.ISIN)
                                                                            .setIdentifierValue(bondModel.getIsin())))
                                                                    .addIdentifier(AssetIdentifier.builder()
                                                                            .setIdentifier(FieldWithMetaString.builder()
                                                                                    .setValue(bondModel.instrumentName))
                                                                            .setIdentifierType(AssetIdTypeEnum.NAME)))))))))
                    .addPriceQuantity(PriceQuantity.builder()
                            .setPrice(List.of(FieldWithMetaPriceSchedule.builder()
                                    .setValue(PriceSchedule.builder()
                                            .setCashPrice(CashPrice.builder()
                                                    .setCashPriceType(CashPriceTypeEnum.FEE))
                                            .setValue(BigDecimal.valueOf(Double.parseDouble(commission)))))))))

            .setCounterparty(List.of(Counterparty.builder()
                    .setPartyReference(ReferenceWithMetaParty.builder()
                            .setExternalReference("Client"))
                    .setRole(CounterpartyRoleEnum.PARTY_1)))
            .addCounterparty(Counterparty.builder()
                    .setPartyReference(ReferenceWithMetaParty.builder()
                            .setExternalReference("Dealer"))
                    .setRole(CounterpartyRoleEnum.PARTY_2))
            .setParty(partyList)
            .setPartyRole(partyRoleList)
            .setExecutionDetails(ExecutionDetails.builder()
                    .setExecutionVenue(LegalEntity.builder()
                            .setEntityId(List.of(FieldWithMetaString.builder()
                                    .setValue(marketid)))
                            .addEntityId(FieldWithMetaString.builder()
                                    .setValue(platform))))
            .build();

    ExecutionInstruction executionInstruction = ExecutionInstruction.builder()
            .setProduct(bond.getNonTransferableProduct())
            .addPriceQuantity(trade.getTradeLot().get(0).getPriceQuantity())
            .addCounterparty(trade.getCounterparty())
            .addParties(trade.getParty())
            .addPartyRoles(trade.getPartyRole())
            .setExecutionDetails(trade.getExecutionDetails())
            .setTradeDate(trade.getTradeDate())
            .addTradeIdentifier(trade.getTradeIdentifier())
            .setLotIdentifier(Identifier.builder()
                    .setAssignedIdentifier(List.of(AssignedIdentifier.builder()
                            .setIdentifierValue("LOT-0"))))
            .build();


    return executionInstruction;
  }

  public  ExecutionInstruction createFutureTrade(Product bondFuture, BondFutureModel bondFutureModel) throws IOException {

    CdmDates cdmDates = new CdmDates();
    CdmBusinessEvent cdmBusinessEvent = new CdmBusinessEvent();

    DateTimeFormatter formatter  = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
    ZonedDateTime tradeDate = ZonedDateTime.parse(bondFutureModel.tradeDate, formatter);
    Date tradeDateStr= Date.of(tradeDate.getYear(), tradeDate.getMonthValue(), tradeDate.getDayOfMonth());

    Party buyerParty = Party.builder()
            .setPartyId(List.of(PartyIdentifier.builder()
                    .setIdentifierValue("ClientId"))).build();


    Party sellerParty = Party.builder()
            .setPartyId(List.of(PartyIdentifier.builder()
                    .setIdentifierValue("DealerId"))).build();

    Party traderParty = Party.builder()
            .setPartyId(List.of(PartyIdentifier.builder()
                    .setIdentifierValue("Trader"))).build();

    List<Party> partyList = List.of(buyerParty, sellerParty, traderParty);

    PartyRole buyer = PartyRole.builder()
            .setRole(PartyRoleEnum.BUYER)
            .setPartyReference(ReferenceWithMetaParty.builder()
                    .setValue(buyerParty))
            .build();

    PartyRole seller = PartyRole.builder()
            .setRole(PartyRoleEnum.SELLER)
            .setPartyReference(ReferenceWithMetaParty.builder()
                    .setValue(sellerParty))
            .build();

    PartyRole trader = PartyRole.builder()
            .setRole(PartyRoleEnum.BOOKING_PARTY)
            .setPartyReference(ReferenceWithMetaParty.builder()
                    .setValue(traderParty))
            .build();

    List<PartyRole> partyRoleList = List.of(buyer, seller, trader);


    Trade trade = Trade.builder()
            .setProduct(bondFuture.getNonTransferableProduct())
            .setTradeDate(FieldWithMetaDate.builder()
                    .setValue(tradeDateStr))
            .setTradeTime(FieldWithMetaTimeZone.builder()
                    .setValue(TimeZone.builder()
                            .setLocation(FieldWithMetaString.builder()
                                    .setValue("UTC"))))
            .setTradeIdentifier(List.of(TradeIdentifier.builder()
                    .setIdentifierType(TradeIdentifierTypeEnum.UNIQUE_TRANSACTION_IDENTIFIER)
                    .setAssignedIdentifier(List.of(AssignedIdentifier.builder()
                            .setIdentifierValue("UTI123")))))
            .setTradeLot(List.of(TradeLot.builder()
                    .setPriceQuantity(List.of(PriceQuantity.builder()
                            .setPrice(List.of(FieldWithMetaPriceSchedule.builder()
                                    .setValue(PriceSchedule.builder()
                                            .setValue(BigDecimal.valueOf(Double.parseDouble(bondFutureModel.price)))
                                            .setUnit(UnitType.builder()
                                                    .setFinancialUnit(FinancialUnitEnum.CONTRACT)
                                                    .setCurrencyValue(bondFutureModel.currency))
                                            .setPerUnitOf(UnitType.builder()
                                                    .setFinancialUnit(FinancialUnitEnum.CONTRACT)
                                                    .setCurrencyValue(bondFutureModel.currency))
                                            .setPriceType(PriceTypeEnum.ASSET_PRICE))))
                            .setQuantity(List.of(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                    .setValue(NonNegativeQuantitySchedule.builder()
                                            .setValue(BigDecimal.valueOf(Double.parseDouble(bondFutureModel.nominalQuantity)))
                                            .setUnit(UnitType.builder()
                                                    .setCurrencyValue(bondFutureModel.currency)))))
                            .setObservable(FieldWithMetaObservable.builder()
                                    .setValue(Observable.builder()
                                            .setAsset(Asset.builder()
                                                    .setInstrument(Instrument.builder()
                                                            .setSecurity(Security.builder()
                                                                    .setIdentifier(List.of(AssetIdentifier.builder()
                                                                            .setIdentifierType(AssetIdTypeEnum.ISIN)
                                                                            .setIdentifierValue(bondFutureModel.underlyingIsin))))))))))))

            .setCounterparty(List.of(Counterparty.builder()
                    .setPartyReference(ReferenceWithMetaParty.builder()
                            .setExternalReference("Client"))
                    .setRole(CounterpartyRoleEnum.PARTY_1)))
            .addCounterparty(Counterparty.builder()
                    .setPartyReference(ReferenceWithMetaParty.builder()
                            .setExternalReference("Dealer"))
                    .setRole(CounterpartyRoleEnum.PARTY_2))
            .setParty(partyList)
            .setPartyRole(partyRoleList)
            .setExecutionDetails(ExecutionDetails.builder()
                    .setExecutionVenue(LegalEntity.builder()
                            .setEntityId(List.of(FieldWithMetaString.builder()
                                    .setValue(bondFutureModel.marketid)))))
            .build();

    ExecutionInstruction executionInstruction = ExecutionInstruction.builder()
            .setProduct(bondFuture.getNonTransferableProduct())
            .addPriceQuantity(trade.getTradeLot().get(0).getPriceQuantity())
            .addCounterparty(trade.getCounterparty())
            .addParties(trade.getParty())
            .addPartyRoles(trade.getPartyRole())
            .setExecutionDetails(trade.getExecutionDetails())
            .setTradeDate(trade.getTradeDate())
            .addTradeIdentifier(trade.getTradeIdentifier())
            .build();


    return executionInstruction;
  }
}
