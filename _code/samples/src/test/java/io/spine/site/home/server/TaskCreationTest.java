/*
 * Copyright 2020, TeamDev. All rights reserved.
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.spine.site.home.server;

import io.spine.server.BoundedContextBuilder;
import io.spine.site.home.Task;
import io.spine.site.home.TaskId;
import io.spine.site.home.TaskItem;
import io.spine.site.home.command.CreateTask;
import io.spine.site.home.event.TaskCreated;
import io.spine.testing.server.blackbox.ContextAwareTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.spine.testing.TestValues.randomString;

@DisplayName("Handling `CreateTask` command should")
public class TaskCreationTest extends ContextAwareTest {

    private TaskId task;
    private String name;
    private String description;

    @Override
    protected BoundedContextBuilder contextBuilder() {
        return NanoPmContext.newBuilder();
    }

    private CreateTask generateCommand() {
        task = TaskId.generate();
        name = randomString();
        description = randomString();
        return CreateTask.newBuilder()
                   .setId(task)
                   .setName(name)
                   .setDescription(description)
                   .vBuild();
    }

    @BeforeEach
    void postCommand() {
        CreateTask cmd = generateCommand();
        context().receivesCommand(cmd);
    }

    @Test
    @DisplayName("generate `TaskCreated` event")
    void eventGenerated() {
        TaskCreated expected = expectedEvent();
        context().assertEvent(TaskCreated.class)
                 .comparingExpectedFieldsOnly()
                 .isEqualTo(expected);
    }

    private TaskCreated expectedEvent() {
        return TaskCreated.newBuilder()
                .setTask(task)
                .setName(name)
                .setDescription(description)
                .buildPartial();
    }

    @Test
    @DisplayName("create a `Task`")
    void aggregateCreation() {
        Task expected = expectedState();
        context().assertEntityWithState(task, Task.class)
                 .hasStateThat()
                 .comparingExpectedFieldsOnly()
                 .isEqualTo(expected);
    }

    private Task expectedState() {
        return Task.newBuilder()
                   .setId(task)
                   .setName(name)
                   .setDescription(description)
                   .buildPartial();
    }

    @Test
    @DisplayName("create a `TaskItem`")
    void projectionCreation() {
        TaskItem expected = TaskItem
                .newBuilder()
                .setName(name)
                .setDescription(description)
                .buildPartial();

        context().assertEntityWithState(task, TaskItem.class)
                 .hasStateThat()
                 .comparingExpectedFieldsOnly()
                 .isEqualTo(expected);
    }
}
