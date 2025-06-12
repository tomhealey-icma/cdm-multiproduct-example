package com.finxis;

import cdm.event.common.*;
import cdm.event.common.functions.Create_BusinessEvent;
import cdm.event.common.functions.Create_Execution;
import cdm.event.common.functions.Create_QuantityChange;
import cdm.event.common.functions.Create_Transfer;
import cdm.event.common.metafields.ReferenceWithMetaTradeState;
import com.finxis.util.CdmDates;
import com.finxis.util.FileWriter;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.CdmRuntimeModule;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CdmBusinessEvent {

  public BusinessEvent runExecutionBusinessEvent(ExecutionInstruction executionInstruction) throws IOException {

    Injector injector = Guice.createInjector(new CdmRuntimeModule());
    FileWriter fileWriter = new FileWriter();

    //Create a primitive execution instruction
    PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder()
            .setExecution(executionInstruction)
            .build();

    Date effectiveDate = executionInstruction.getTradeDate().getValue();
    Date eventDate = executionInstruction.getTradeDate().getValue();

    Create_Execution.Create_ExecutionDefault execution = new Create_Execution.Create_ExecutionDefault();
    injector.injectMembers(execution);
    TradeState tradeState = execution.evaluate(executionInstruction);

    //Create an instruction from primitive. Before state is null
    Instruction instruction = Instruction.builder()
            .setPrimitiveInstruction(primitiveInstruction)
            .setBefore(null)
            .build();

    List<Instruction> instructionList = List.of(instruction);

    DateTimeFormatter eventDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
    LocalDateTime localDateTime = LocalDateTime.now();
    String eventDateTime = localDateTime.format(eventDateFormat);

    Create_BusinessEvent be = new Create_BusinessEvent.Create_BusinessEventDefault();
    injector.injectMembers(be);
    BusinessEvent businessEvent = be.evaluate(instructionList, null, eventDate, effectiveDate);

    String businessEventJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(businessEvent);
    System.out.println(businessEventJson);
    fileWriter.writeEventToFile(executionInstruction.getProduct().getIdentifier().get(0).getIdentifier().getValue()+"-execution-event", eventDateTime, businessEventJson);

    return businessEvent;

  }

  public BusinessEvent runChangeQuantityBusinessEvent(TradeState originalTradeState, QuantityChangeInstruction quantityChangeInstruction) throws IOException {

    Injector injector = Guice.createInjector(new CdmRuntimeModule());
    FileWriter fileWriter = new FileWriter();

    //Create a primitive  instruction

    Date effectiveDate = new CdmDates().createCurrentDate();
    Date eventDate = new CdmDates().createCurrentDate();

    Create_QuantityChange.Create_QuantityChangeDefault change = new Create_QuantityChange.Create_QuantityChangeDefault();
    injector.injectMembers(change);
    TradeState tradeState = change.evaluate(quantityChangeInstruction,originalTradeState);


    PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder()
            .setQuantityChange(quantityChangeInstruction)
            .build();

    //Create an instruction from primitive. Before state is null
    Instruction instruction = Instruction.builder()
            .setPrimitiveInstruction(primitiveInstruction)
            .setBefore(ReferenceWithMetaTradeState.builder()
                    .setValue(originalTradeState))
            .build();
    

    List<Instruction> instructionList = List.of(instruction);

    DateTimeFormatter eventDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
    LocalDateTime localDateTime = LocalDateTime.now();
    String eventDateTime = localDateTime.format(eventDateFormat);

    Create_BusinessEvent be = new Create_BusinessEvent.Create_BusinessEventDefault();
    injector.injectMembers(be);
    BusinessEvent businessEvent = be.evaluate(instructionList, null, eventDate, effectiveDate);

    String businessEventJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(businessEvent);
    System.out.println(businessEventJson);
    fileWriter.writeEventToFile(tradeState.getTrade().getProduct().getIdentifier().get(0).getIdentifier().getValue()+"-change-event", eventDateTime, businessEventJson);

    return businessEvent;

  }

  public BusinessEvent runTransferBusinessEvent(TradeState originalTradeState, TransferInstruction transferInstruction) throws IOException {

    Injector injector = Guice.createInjector(new CdmRuntimeModule());
    FileWriter fileWriter = new FileWriter();

    //Create a primitive  instruction
    PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder()
            .setTransfer(transferInstruction);

    Date effectiveDate = new CdmDates().createCurrentDate();
    Date eventDate = new CdmDates().createCurrentDate();

    Create_Transfer.Create_TransferDefault transfer = new Create_Transfer.Create_TransferDefault();
    injector.injectMembers(transfer);
    TradeState tradeState = transfer.evaluate(transferInstruction,originalTradeState);

    //Create an instruction from primitive. Before state is null
    Instruction instruction = Instruction.builder()
            .setPrimitiveInstruction(primitiveInstruction)
            .setBefore(ReferenceWithMetaTradeState.builder()
                    .setValue(originalTradeState))
            .build();

    List<Instruction> instructionList = List.of(instruction);

    DateTimeFormatter eventDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
    LocalDateTime localDateTime = LocalDateTime.now();
    String eventDateTime = localDateTime.format(eventDateFormat);

    Create_BusinessEvent be = new Create_BusinessEvent.Create_BusinessEventDefault();
    injector.injectMembers(be);
    BusinessEvent businessEvent = be.evaluate(instructionList, null, eventDate, effectiveDate);

    String businessEventJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(businessEvent);
    System.out.println(businessEventJson);
    fileWriter.writeEventToFile(tradeState.getTrade().getProduct().getIdentifier().get(0).getIdentifier().getValue()+"-transfer-event", eventDateTime, businessEventJson);

    return businessEvent;

  }

}
