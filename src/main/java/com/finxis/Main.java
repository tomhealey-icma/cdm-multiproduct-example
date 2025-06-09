package com.finxis;

import cdm.base.datetime.*;
import cdm.base.datetime.daycount.DayCountFractionEnum;
import cdm.base.datetime.daycount.metafields.FieldWithMetaDayCountFractionEnum;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.base.datetime.metafields.FieldWithMetaTimeZone;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.base.math.*;
import cdm.base.math.metafields.FieldWithMetaNonNegativeQuantitySchedule;
import cdm.base.math.metafields.ReferenceWithMetaNonNegativeQuantitySchedule;
import cdm.base.staticdata.asset.common.*;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.TradeIdentifierTypeEnum;
import cdm.base.staticdata.party.*;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.*;
import cdm.event.common.functions.Create_Execution;
import cdm.observable.asset.*;
import cdm.observable.asset.metafields.FieldWithMetaObservable;
import cdm.observable.asset.metafields.FieldWithMetaPriceSchedule;
import cdm.observable.asset.metafields.ReferenceWithMetaPriceSchedule;
import cdm.product.asset.*;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.PayRelativeToEnum;
import cdm.product.common.schedule.PaymentDates;
import cdm.product.common.schedule.RateSchedule;
import cdm.product.common.settlement.ResolvablePriceQuantity;
import cdm.product.common.settlement.SettlementDate;
import cdm.product.common.settlement.SettlementTerms;
import cdm.product.template.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finxis.data.InputData;
import com.finxis.models.*;
import com.finxis.product.BuildProduct;
import com.finxis.trade.CreateTrade;
import com.finxis.util.FileWriter;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;
import com.rosetta.model.metafields.FieldWithMetaString;
import com.rosetta.model.metafields.MetaFields;
import org.finos.cdm.CdmRuntimeModule;

import cdm.event.common.*;
import cdm.event.common.functions.*;
import cdm.event.common.metafields.ReferenceWithMetaCollateralPortfolio;
import cdm.event.common.metafields.ReferenceWithMetaTrade;
import cdm.event.common.metafields.ReferenceWithMetaTradeState;
import cdm.event.qualification.functions.*;
import cdm.event.workflow.*;
import cdm.event.workflow.functions.Create_WorkflowStep;
import com.finxis.data.InputData.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("CDM Bond Demo");

        Main main = new Main();
        InputData inputData = new InputData();
        BuildProduct buildProduct = new BuildProduct();
        CreateTrade  createTrade = new CreateTrade();
        FileWriter fileWriter = new FileWriter();
        DateTimeFormatter eventDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        String eventDateTime = localDateTime.format(eventDateFormat);


        //European Corporate Bond Example
        BondModel bondModel = inputData.setBondData();
        Product product = buildProduct.createBondProduct(bondModel);
        Trade trade = createTrade.createBondTrade(product, bondModel);

        String tradeJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(trade);
        System.out.println(tradeJson);
        fileWriter.writeEventToFile("eu-corporate-bond-product", eventDateTime, tradeJson);


        ObjectMapper rosettaObjectMapper = RosettaObjectMapper.getNewRosettaObjectMapper();
        Trade tradeObj = new Trade.TradeBuilderImpl();
        Trade newTrade = rosettaObjectMapper.readValue(tradeJson, tradeObj.getClass());

        //US Treasury Example
        BondModel usTreasuryModel = inputData.setUSTreasuryBondData();
        product = buildProduct.createBondProduct(usTreasuryModel);
        trade = createTrade.createBondTrade(product, usTreasuryModel);

        tradeJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(trade);
        System.out.println(tradeJson);
        fileWriter.writeEventToFile("us-treasury-product", eventDateTime, tradeJson);


        rosettaObjectMapper = RosettaObjectMapper.getNewRosettaObjectMapper();
        tradeObj = new Trade.TradeBuilderImpl();
        newTrade = rosettaObjectMapper.readValue(tradeJson, tradeObj.getClass());

        //Bond Future
        BondFutureModel bondFutureModel = inputData.setBondFutureData();
        product = buildProduct.createBondFutureProduct(bondFutureModel);
        trade = createTrade.createFutureTrade(product, bondFutureModel);

        tradeJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(trade);
        System.out.println(tradeJson);
        fileWriter.writeEventToFile("bond-future-product", eventDateTime, tradeJson);

        rosettaObjectMapper = RosettaObjectMapper.getNewRosettaObjectMapper();
        tradeObj = new Trade.TradeBuilderImpl();
        newTrade = rosettaObjectMapper.readValue(tradeJson, tradeObj.getClass());


        //FX Cash

        FXCashModel fxCashModel = inputData.setFXCashData();
        product = buildProduct.createFXCashProduct(fxCashModel);
        TradeModel fxTradeModel = inputData.setFXCashTradeData();
        trade = createTrade.createFXCashTrade(product, fxTradeModel);

        tradeJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(trade);
        System.out.println(tradeJson);
        fileWriter.writeEventToFile("fxcash-product", eventDateTime, tradeJson);

        rosettaObjectMapper = RosettaObjectMapper.getNewRosettaObjectMapper();
        tradeObj = new Trade.TradeBuilderImpl();
        newTrade = rosettaObjectMapper.readValue(tradeJson, tradeObj.getClass());

        //FX Option

        FXOptionModel fxOptionModel = inputData.setFXOptionData();
        product = buildProduct.createFXOptionProduct(fxOptionModel);
        TradeModel fxOptionTradeModel = inputData.setFXOptionTradeData();
        trade = createTrade.createFXOptionTrade(product, fxOptionTradeModel);

        tradeJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(trade);
        System.out.println(tradeJson);
        fileWriter.writeEventToFile("fxoption-product", eventDateTime, tradeJson);

        rosettaObjectMapper = RosettaObjectMapper.getNewRosettaObjectMapper();
        tradeObj = new Trade.TradeBuilderImpl();
        newTrade = rosettaObjectMapper.readValue(tradeJson, tradeObj.getClass());



        //FX Option

        IrsOtcModel irsOtcModel = inputData.setIrsOctData();
        product = buildProduct.createIrsOtcProduct(irsOtcModel);
        TradeModel irsOtcTradeModel = inputData.setIrsOtcTradeData();
        trade = createTrade.createIrsOtcTrade(product, irsOtcTradeModel);

        tradeJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(trade);
        System.out.println(tradeJson);
        fileWriter.writeEventToFile("irsotc-product", eventDateTime, tradeJson);

        rosettaObjectMapper = RosettaObjectMapper.getNewRosettaObjectMapper();
        tradeObj = new Trade.TradeBuilderImpl();
        newTrade = rosettaObjectMapper.readValue(tradeJson, tradeObj.getClass());

    }


































}
