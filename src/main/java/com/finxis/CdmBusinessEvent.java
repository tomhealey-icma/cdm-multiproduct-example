package com.finxis;

import cdm.event.common.*;
import cdm.event.common.functions.Create_BusinessEvent;
import cdm.event.common.functions.Create_Execution;
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

  public void runExecutionBusinessEvent(ExecutionInstruction executionInstruction) throws IOException {

    Injector injector = Guice.createInjector(new CdmRuntimeModule());
    FileWriter fileWriter = new FileWriter();

    //Create a primitive execution instruction
    PrimitiveInstruction primitiveInstruction = PrimitiveInstruction.builder()
            .setExecution(executionInstruction);

    Date effectiveDate = executionInstruction.getTradeDate().getValue();
    Date eventDate = executionInstruction.getTradeDate().getValue();

    Create_Execution.Create_ExecutionDefault repoExecution = new Create_Execution.Create_ExecutionDefault();
    injector.injectMembers(repoExecution);
    TradeState tradeState = repoExecution.evaluate(executionInstruction);

    //Create an instruction from primitive. Before state is null
    Instruction instruction = Instruction.builder()
            .setPrimitiveInstruction(primitiveInstruction)
            .setBefore(null)
            .build();

    List<Instruction> instructionList = List.of(instruction);

    DateTimeFormatter eventDateFormat = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
    LocalDateTime localDateTime = LocalDateTime.now();
    String eventDateTime = localDateTime.format(eventDateFormat);

    BusinessEvent betest = BusinessEvent.builder()
            .addInstruction(instruction)
            .build();

    Create_BusinessEvent be = new Create_BusinessEvent.Create_BusinessEventDefault();
    injector.injectMembers(be);
    BusinessEvent businessEvent = be.evaluate(instructionList, null, eventDate, effectiveDate);


    String businessEventJson = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(businessEvent);
    System.out.println(businessEventJson);
    fileWriter.writeEventToFile(executionInstruction.getProduct().getIdentifier().get(0).getIdentifier().getValue()+"-execution-event", eventDateTime, businessEventJson);
  }

}
