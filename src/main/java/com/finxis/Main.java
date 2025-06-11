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
import cdm.base.staticdata.identifier.Identifier;
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
import cdm.product.common.settlement.AssetFlowBase;
import cdm.product.common.settlement.ResolvablePriceQuantity;
import cdm.product.common.settlement.SettlementDate;
import cdm.product.common.settlement.SettlementTerms;
import cdm.product.template.*;
import cdm.product.template.metafields.ReferenceWithMetaPayout;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finxis.data.InputData;
import com.finxis.models.*;
import com.finxis.product.BuildProduct;
import com.finxis.trade.CreateTrade;
import com.finxis.util.CdmDates;
import com.finxis.util.FileWriter;
import com.finxis.workflows.ExecutionWorkflow;
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
        CdmBusinessEvent cdmBusinessEvent = new CdmBusinessEvent();
        BusinessEvent businessEvent = BusinessEvent.builder().build();


        //European Corporate Bond Example
        BondModel bondModel = inputData.setBondData();
        Product product = buildProduct.createBondProduct(bondModel);
        ExecutionInstruction executionInstruction = createTrade.createBondTrade(product, bondModel);
        cdmBusinessEvent.runExecutionBusinessEvent(executionInstruction);


        //US Treasury Example
        //Create product and trade
        BondModel usTreasuryModel = inputData.setUSTreasuryBondData();
        product = buildProduct.createBondProduct(usTreasuryModel);
        executionInstruction = createTrade.createBondTrade(product, usTreasuryModel);
        businessEvent = cdmBusinessEvent.runExecutionBusinessEvent(executionInstruction);

        Identifier workflowIdentifier = Identifier.builder()
                .setAssignedIdentifier(List.of(AssignedIdentifier.builder()
                        .setIdentifierValue("1000")))
                .build();
        WorkflowStep executionWorkflowStep = WorkflowStep.builder()
                                .setEventIdentifier(List.of(workflowIdentifier))
                                .setWorkflowState(WorkflowState.builder()
                                        .setWorkflowStatus(WorkflowStatusEnum.ACCEPTED))
                                        .setAction(ActionEnum.NEW)
                                        .setBusinessEvent(businessEvent)
                .build();



        Workflow workflow = Workflow.builder()
                .setSteps(List.of(executionWorkflowStep))
                .build();

        String workFlowJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(workflow);
        System.out.println(workFlowJson);
        fileWriter.writeEventToFile(usTreasuryModel.isin + "-execution-workflow", eventDateTime, workFlowJson);


        //Correction
        workflowIdentifier = Identifier.builder()
                .setAssignedIdentifier(List.of(AssignedIdentifier.builder()
                        .setIdentifierValue("1001")))

                .build();

        QuantityChangeInstruction quantityChangeInstruction = QuantityChangeInstruction.builder()
                .setDirection(QuantityChangeDirectionEnum.REPLACE)
                .setChange(List.of(PriceQuantity.builder()
                                .setQuantity(List.of(FieldWithMetaNonNegativeQuantitySchedule.builder()
                                        .setValue(NonNegativeQuantitySchedule.builder()
                                                .setValue(BigDecimal.valueOf(Double.parseDouble("10000000.00")))
                                                .setUnit(UnitType.builder()
                                                        .setCurrencyValue(bondModel.currency)))))))

                .build();


        businessEvent = cdmBusinessEvent.runChangeQuantityBusinessEvent(businessEvent.getAfter().get(0), quantityChangeInstruction);

        WorkflowStep correctWorkFlowStep = WorkflowStep.builder()
                .setEventIdentifier(List.of(workflowIdentifier))
                .setWorkflowState(WorkflowState.builder()
                        .setWorkflowStatus(WorkflowStatusEnum.ACCEPTED))
                .setAction(ActionEnum.CORRECT)
                .setBusinessEvent(businessEvent)
                .setLineage(Lineage.builder()
                        .setEventReferenceValue(List.of(executionWorkflowStep))
                        .build())
                .build();


        workflow = Workflow.builder()
                .setSteps(List.of(executionWorkflowStep,correctWorkFlowStep))
                .build();

        workFlowJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(workflow);
        System.out.println(workFlowJson);
        fileWriter.writeEventToFile(usTreasuryModel.isin + "-correction-workflow", eventDateTime, workFlowJson);


        //Transfer Step
        workflowIdentifier = Identifier.builder()
                .setAssignedIdentifier(List.of(AssignedIdentifier.builder()
                        .setIdentifierValue("1002")))

                .build();
        TransferInstruction transferInstruction = TransferInstruction.builder()
                .setTransferState(List.of(TransferState.builder()
                                .setTransferStatus(TransferStatusEnum.SETTLED)
                                .setTransfer(Transfer.builder()
                                        .setSettlementOrigin(ReferenceWithMetaPayout.builder()
                                                .setValue(product.getNonTransferableProduct().getEconomicTerms().getPayout()
                                                        .get(0).getSettlementPayout().getUnderlier().getProduct().getTransferableProduct()
                                                        .getEconomicTerms().getPayout().get(0))))))


                        .build();

        businessEvent = cdmBusinessEvent.runChangeQuantityBusinessEvent(businessEvent.getAfter().get(0), quantityChangeInstruction);

        WorkflowStep transferWorkFlowStep = WorkflowStep.builder()
                .setEventIdentifier(List.of(workflowIdentifier))
                .setWorkflowState(WorkflowState.builder()
                        .setWorkflowStatus(WorkflowStatusEnum.ACCEPTED))
                .setAction(ActionEnum.NEW)
                .setBusinessEvent(businessEvent)
                .build();

        workflow = Workflow.builder()
                .setSteps(List.of(executionWorkflowStep,correctWorkFlowStep, transferWorkFlowStep))
                .build();

        workFlowJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(workflow);
        System.out.println(workFlowJson);
        fileWriter.writeEventToFile(usTreasuryModel.isin + "-transfer-workflow", eventDateTime, workFlowJson);



        //Bond Future
        BondFutureModel bondFutureModel = inputData.setBondFutureData();
        product = buildProduct.createBondFutureProduct(bondFutureModel);
        executionInstruction = createTrade.createFutureTrade(product, bondFutureModel);
        businessEvent = cdmBusinessEvent.runExecutionBusinessEvent(executionInstruction);



        //FX Cash

        FXCashModel fxCashModel = inputData.setFXCashData();
        product = buildProduct.createFXCashProduct(fxCashModel);
        TradeModel fxTradeModel = inputData.setFXCashTradeData();
        executionInstruction = createTrade.createFXCashTrade(product, fxTradeModel);
        businessEvent = cdmBusinessEvent.runExecutionBusinessEvent(executionInstruction);


        //FX Option

        FXOptionModel fxOptionModel = inputData.setFXOptionData();
        product = buildProduct.createFXOptionProduct(fxOptionModel);
        TradeModel fxOptionTradeModel = inputData.setFXOptionTradeData();
        executionInstruction = createTrade.createFXOptionTrade(product, fxOptionTradeModel);
        businessEvent = cdmBusinessEvent.runExecutionBusinessEvent(executionInstruction);



        //FX Option

        IrsOtcModel irsOtcModel = inputData.setIrsOctData();
        product = buildProduct.createIrsOtcProduct(irsOtcModel);
        TradeModel irsOtcTradeModel = inputData.setIrsOtcTradeData();
        executionInstruction = createTrade.createIrsOtcTrade(product, irsOtcTradeModel);
        businessEvent = cdmBusinessEvent.runExecutionBusinessEvent(executionInstruction);


    }


































}
