package com.finxis.workflows;

import cdm.base.staticdata.identifier.Identifier;
import cdm.event.common.ActionEnum;
import cdm.event.common.BusinessEvent;
import cdm.event.common.EventIntentEnum;
import cdm.event.workflow.*;

import java.util.List;

public class ExecutionWorkflow {

  //This class defines an execution workflow with workflow steps and events.

  public Workflow getExecutionWorkflow(){
    Workflow executionWorkFlow = Workflow.builder()
            .setSteps(List.of(getExecutionStep(),getConfirmationStep(),getTransferStep()))
            .build();

    return executionWorkFlow;
  }

  public WorkflowStep getExecutionStep(){

    WorkflowStep workflowStep = WorkflowStep.builder()
            .setProposedEvent(EventInstruction.builder()
                    .setIntent(EventIntentEnum.CONTRACT_FORMATION))
            .build();
    return workflowStep;

  }

  public WorkflowStep setExecutionEvent(WorkflowStep ws, BusinessEvent be, Identifier id){

    WorkflowStep wfs = WorkflowStep.builder()
            .setBusinessEvent(be)
            .setPreviousWorkflowStepValue(null)
            .setEventIdentifier(List.of(id))
            .setAction(ActionEnum.NEW)
            .setWorkflowState(WorkflowState.builder()
                    .setWorkflowStatus(WorkflowStatusEnum.ACCEPTED))
             .build();

    return wfs;

  }

  public WorkflowStep correctExecutionEvent(WorkflowStep ws, WorkflowStep priorEvent, BusinessEvent be, Identifier id){

    ws.toBuilder()
            .setBusinessEvent(be)
            .setPreviousWorkflowStepValue(priorEvent)
            .setEventIdentifier(List.of(id))
            .setAction(ActionEnum.CORRECT)
            .setWorkflowState(WorkflowState.builder()
                    .setWorkflowStatus(WorkflowStatusEnum.ACCEPTED))
            .build();

    return ws;

  }

  public WorkflowStep getConfirmationStep(){

    WorkflowStep workflowStep = WorkflowStep.builder()
            .build();
    return workflowStep;

  }

  public WorkflowStep setConfirmationEvent(WorkflowStep ws, BusinessEvent be, Identifier id){

    ws.toBuilder()
            .setBusinessEvent(be)
            .setPreviousWorkflowStepValue(null)
            .setEventIdentifier(List.of(id))
            .setAction(ActionEnum.NEW)
            .setWorkflowState(WorkflowState.builder()
                    .setWorkflowStatus(WorkflowStatusEnum.CONFIRMED))
            .build();

    return ws;

  }

  public WorkflowStep getTransferStep(){

    WorkflowStep workflowStep = WorkflowStep.builder()
            .build();
    return workflowStep;

  }

  public WorkflowStep setTransferEvent(WorkflowStep ws, BusinessEvent be, Identifier id){

    ws.toBuilder()
            .setBusinessEvent(be)
            .setPreviousWorkflowStepValue(null)
            .setEventIdentifier(List.of(id))
            .setAction(ActionEnum.NEW)
            .setWorkflowState(WorkflowState.builder()
                    .setWorkflowStatus(WorkflowStatusEnum.SUBMITTED))
            .build();

    return ws;

  }


}
