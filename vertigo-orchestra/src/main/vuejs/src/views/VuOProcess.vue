<template>
  <q-splitter v-model="splitterModel" style="height: 85vh" :limits="[0, 100]">
    <template v-slot:before>
      <div class="q-pa-md text-primary">
        <div class="text-h4 q-mb-md">
          <div v-if="processInfo.label || errorMessage"> {{ processInfo.label || errorMessage }} </div>
          <div v-else>
            <q-spinner-ios color="primary" size="sm"></q-spinner-ios>
          </div>
        </div>

        <q-card class="my-card">
          <q-card-section>
            <div class="text-h6">{{ $q.lang.vuiOrchestra.orchestra.totalExecutions }}</div>
          </q-card-section>

          <q-separator inset></q-separator>

          <q-card-section class="row">
            <div class="col">
              <q-list>
                <q-item>
                  <q-item-section>
                    <div class="text-weight-medium">
                      <q-icon color="green" name="done" size="sm"></q-icon>
                      {{ processSummary.successfulCount }}
                    </div>
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
            <div class="col">
              <q-list>
                <q-item>
                  <q-item-section>
                    <div class="text-weight-medium">
                      <q-icon color="red" name="error" size="sm"></q-icon>
                      {{ processSummary.errorsCount }}
                    </div>
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
            <div class="col">
              <q-list>
                <q-item>
                  <q-item-section>
                    <div class="text-weight-medium">
                      <q-icon color="grey" name="timer_off" size="sm"></q-icon>
                      {{ processSummary.misfiredCount }}
                    </div>
                  </q-item-section>
                </q-item>
              </q-list>
            </div>
          </q-card-section>
          <q-separator inset></q-separator>
          <q-card-section v-if="!orchestraUiReadOnly">
            <q-btn :label="$q.lang.vuiOrchestra.orchestra.executeNow" @click="executeNow(processInfo.definitionId)" color="indigo" class="q-mx-auto" />
          </q-card-section>
        </q-card>

        <q-card class="my-card q-mt-lg  text-primary" v-if="processInfo.metadatas && Object.keys(processInfo.metadatas).length>0">
          <q-card-section>
            <div class="text-h6"> {{ $q.lang.vuiOrchestra.orchestra.functionalId }}</div>
          </q-card-section>

          <q-separator inset></q-separator>

          <q-card-section>
            <q-list dense>
              <div v-for="(value, name) in processInfo.metadatas" :key="name">
                <q-item>
                  <q-item-section class="text-weight-medium">{{ name }}</q-item-section>
                  <q-item-section side>{{ value }}</q-item-section>
                </q-item>
              </div>
            </q-list>
          </q-card-section>
        </q-card>

        <q-card class="my-card q-mt-lg  text-primary">
          <q-card-section>
            <div class="text-h6">
              {{ $q.lang.vuiOrchestra.orchestra.technicalId }}
              <q-btn  round color="indigo" icon="edit" class="q-ml-sm" size="sm" @click="editMode.technical = !editMode.technical" v-if="!orchestraUiReadOnly" ></q-btn>
            </div>
          </q-card-section>

          <q-separator inset></q-separator>
          <q-card-section class="q-gutter-sm">
            <div>
              <q-list dense>                
                <div v-if="editMode.technical">
                  <q-form @submit="onSubmit('technical')">
                    <q-item>
                      <q-item-section class="text-weight-medium"> {{ $q.lang.vuiOrchestra.orchestra.processName }}  </q-item-section>
                      <q-item-section side> {{ processInfo.definitionId }} </q-item-section>
                    </q-item>
                    <q-item>
                      <q-item-section class="text-weight-medium"> {{ $q.lang.vuiOrchestra.orchestra.cronExpression }}  </q-item-section>
                      <q-item-section side>
                        <q-input  v-model="form.technical.cronExpression" :dense="true"  placeholder="* * * * * ? *" />
                      </q-item-section>
                    </q-item>
                    <q-item>
                      <q-item-section class="text-weight-medium"> {{ $q.lang.vuiOrchestra.orchestra.active }} </q-item-section>
                      <q-item-section side>
                        <div class="q-gutter-sm">
                          <q-radio size="xs"   v-model="form.technical.active"  val="true" :label="$q.lang.vuiOrchestra.orchestra.yes" />
                          <q-radio size="xs" v-model="form.technical.active"  val="false" :label="$q.lang.vuiOrchestra.orchestra.no" />
                        </div>
                      </q-item-section>
                    </q-item>
                    <q-item>
                      <q-item-section class="text-weight-medium">  {{ $q.lang.vuiOrchestra.orchestra.multiExecution }}  </q-item-section>
                      <q-item-section side>
                        <div class="q-gutter-sm">
                          <q-radio  size="xs"  v-model="form.technical.multiExecution"  val="true"  :label="$q.lang.vuiOrchestra.orchestra.yes" />
                          <q-radio  size="xs"  v-model="form.technical.multiExecution"  val="false"  :label="$q.lang.vuiOrchestra.orchestra.no" />
                        </div>
                      </q-item-section>
                    </q-item>
                    <q-item>
                      <q-item-section class="text-weight-medium"> {{ $q.lang.vuiOrchestra.orchestra.rescuePeriod }}  </q-item-section>
                      <q-item-section side>
                        <q-input v-model.number="form.technical.rescuePeriod"  type="number" :dense="true" />
                      </q-item-section>
                    </q-item>
                    <div>
                      <q-item>
                        <q-btn :label="$q.lang.vuiOrchestra.orchestra.submit" type="submit"  color="indigo"  class="q-mx-auto" v-if="!orchestraUiReadOnly"/>
                      </q-item>
                    </div>
                  </q-form>
                </div>

                <div v-else>
                  <q-item>
                    <q-item-section class="text-weight-medium"> {{ $q.lang.vuiOrchestra.orchestra.processName }}  </q-item-section>
                    <q-item-section side> {{ processInfo.definitionId }} </q-item-section>
                  </q-item>                    
                  <q-item>
                    <q-item-section class="text-weight-medium"> {{ $q.lang.vuiOrchestra.orchestra.cronExpression }}</q-item-section>
                    <q-item-section side> {{ processInfo.triggeringStrategy ? processInfo.triggeringStrategy.cronExpression : "-" }} </q-item-section>
                  </q-item>
                  <q-item>
                    <q-item-section class="text-weight-medium"> {{ $q.lang.vuiOrchestra.orchestra.active }}  </q-item-section>
                    <q-item-section side> {{ Object.entries(processInfo).length ? processInfo.active ? $q.lang.vuiOrchestra.orchestra.yes : $q.lang.vuiOrchestra.orchestra.no : "-" }}  </q-item-section>
                  </q-item>
                  <q-item>
                    <q-item-section class="text-weight-medium"> {{ $q.lang.vuiOrchestra.orchestra.multiExecution }} </q-item-section>
                    <q-item-section side>
                      {{ processInfo.triggeringStrategy ? processInfo.triggeringStrategy.multiExecution ? $q.lang.vuiOrchestra.orchestra.yes : $q.lang.vuiOrchestra.orchestra.no  : "-"  }}
                    </q-item-section>
                  </q-item>

                  <q-item>
                    <q-item-section class="text-weight-medium"> {{ $q.lang.vuiOrchestra.orchestra.rescuePeriod }} </q-item-section>
                    <q-item-section side>
                      <!-- Temps de validité d'une planification = rescuePeriodInSeconds -->
                      {{ processInfo.triggeringStrategy  ? processInfo.triggeringStrategy.rescuePeriodInSeconds : "-"}}
                    </q-item-section>
                  </q-item>
                </div>
              </q-list>
            </div>
          </q-card-section>
        </q-card>

        <q-card class="my-card q-mt-lg  text-primary ">
          <q-card-section>
            <div class="text-h6">
              {{ $q.lang.vuiOrchestra.orchestra.settings }}
              <q-btn round color="indigo"  icon="edit"  class="q-ml-sm" size="sm" @click="editMode.settings = !editMode.settings" v-if="!orchestraUiReadOnly"></q-btn>
            </div>
          </q-card-section>

          <q-separator inset></q-separator>

          <q-card-section>
            <q-list dense>
              <div v-if=" processInfo.triggeringStrategy && processInfo.triggeringStrategy.initialParams " >
                <div v-if="editMode.settings">
                  <q-form @submit="onSubmit('settings')">
                    <div v-for="(value, name) in processInfo.triggeringStrategy.initialParams" :key="name" >
                      <q-item>
                        <q-item-section class="text-weight-medium">{{ name }}</q-item-section>
                        <q-item-section side>
                          <q-input v-model="form.settings[name]" :dense="true"
                            :rules="[
                              (val) =>
                                (val && val.length > 0) ||
                                $q.lang.vuiOrchestra.orchestra.fieldCannotBeEmpty,
                            ]"
                          />
                        </q-item-section>
                      </q-item>
                    </div>
                    <div>
                      <q-item>
                        <q-btn :label="$q.lang.vuiOrchestra.orchestra.submit" type="submit" color="indigo" class="q-mx-auto" v-if="!orchestraUiReadOnly"/>
                      </q-item>
                    </div>
                  </q-form>
                </div>
                <div v-else>
                  <div  v-for="(value, name) in processInfo.triggeringStrategy.initialParams"  :key="name"  >
                    <q-item>
                      <q-item-section class="text-weight-medium">{{ name }}</q-item-section>
                      <q-item-section side>{{ value }}</q-item-section>
                    </q-item>
                  </div>
                </div>
              </div>
            </q-list>
          </q-card-section>
        </q-card>
      </div>
    </template>
    <template v-slot:after>
      <div class="q-pa-md">
        <div class="text-h4 q-gutter-x-md q-mx-auto row">
          <div class="col">{{ $q.lang.vuiOrchestra.orchestra.executions }}</div>
          <div class="col-auto">
            <q-tabs v-model="filterTab" inline-label class="text-primary q-mx-auto" >
              <q-tab name="all" icon="list" :label="$q.lang.vuiOrchestra.orchestra.all" @click="updateExecutions('')" ></q-tab>
              <q-tab name="done" icon="done" :label="$q.lang.vuiOrchestra.orchestra.done" @click="updateExecutions('DONE')" class="text-green" ></q-tab>
              <q-tab name="error" icon="error" :label="$q.lang.vuiOrchestra.orchestra.error" @click="updateExecutions('ERROR')" class="text-red" ></q-tab>
            </q-tabs>
          </div>
        </div>
        <q-infinite-scroll @load="onLoad" :offset="50" scroll-target="div.q-splitter__panel.q-splitter__after.col" >
          <q-list bordered class="rounded-borders q-mt-sm">
            <div v-for="execution in executions" :key="execution.preId">
              <q-expansion-item expand-separator  @show="fetchActivities(execution.preId)" >
                <template v-slot:header>
                  <q-item-section avatar>
                    <q-avatar  :icon="getIconFromExecutionState(execution.status)"  :color="getColorFromExecutionState(execution.status)" text-color="white"></q-avatar>
                    <q-tooltip> {{ execution.status }}</q-tooltip>
                  </q-item-section>
                  <q-item-section class="text-weight-medium">{{ formatDate(execution.beginTime) }} <q-tooltip :delay="1000" anchor="top middle"> preId: {{ execution.preId }}</q-tooltip></q-item-section>
                </template>
                <q-card>
                  <q-separator></q-separator>
                  <q-card-section>
                    <q-splitter v-model="splitterModelExecutions">
                      <template v-slot:before>
                        <q-tabs v-model="tabs[execution.preId]" vertical class="text-primary" >
                          <q-tab name="info" :label="$q.lang.vuiOrchestra.orchestra.informations" ></q-tab>
                          <q-tab name="activities" :label="$q.lang.vuiOrchestra.orchestra.activities" ></q-tab>
                          <q-tab name="support" :label="$q.lang.vuiOrchestra.orchestra.support" ></q-tab>
                        </q-tabs>
                      </template>
                      <template v-slot:after>
                        <q-tab-panels  v-model="tabs[execution.preId]" animated vertical transition-prev="jump-up"  transition-next="jump-up" >
                          <q-tab-panel name="info">
                            <div class="text-h5">{{ $q.lang.vuiOrchestra.orchestra.informations }}</div>
                            <q-separator class="q-mt-sm q-mb-md"></q-separator>
                            <div style="max-width: 400px;" class="text-primary">
                              <q-list dense>
                                <q-item>
                                  <q-item-section>
                                    <div class="text-weight-medium"> {{ $q.lang.vuiOrchestra.orchestra.startTime }} </div>
                                  </q-item-section>
                                  <q-item-section side class="text-primary"> {{ formatDate(execution.beginTime) }} </q-item-section>
                                </q-item>
                                <q-item>
                                  <q-item-section>
                                    <div class="text-weight-medium"> {{ $q.lang.vuiOrchestra.orchestra.endTime }} </div>
                                  </q-item-section>
                                  <q-item-section side class="text-primary"> {{ formatDate(execution.endTime) }} </q-item-section>
                                </q-item>
                                <q-item>
                                  <q-item-section>
                                    <div class="text-weight-medium">  {{ $q.lang.vuiOrchestra.orchestra.duration }} </div>
                                  </q-item-section>
                                  <q-item-section side class="text-primary"> {{ execution.executionTime }}s </q-item-section>
                                </q-item>
                              </q-list>
                            </div>
                          </q-tab-panel>
                          <q-tab-panel name="activities">
                            <div class="text-h5"> {{ $q.lang.vuiOrchestra.orchestra.activities }} </div>
                            <q-separator class="q-mt-sm q-mb-md"></q-separator>
                            <q-list bordered class="rounded-borders">
                              <div  v-for="activity in activities[execution.preId]" :key="activity.aceId" >
                                <q-expansion-item expand-separator>
                                  <template v-slot:header>
                                    <q-item-section avatar>
                                      <q-avatar :icon="getIconFromExecutionState(activity.status)" :color="getColorFromExecutionState(activity.status)"  text-color="white" ></q-avatar>
                                      <q-tooltip>  {{ activity.status }} </q-tooltip>
                                    </q-item-section>
                                    <q-item-section class="text-weight-medium"> {{ activity.label }} <q-tooltip :delay="1000" anchor="top middle"> aceId: {{ activity.aceId }}</q-tooltip></q-item-section>
                                  </template>
                                  <q-card>
                                    <q-separator></q-separator>
                                    <q-card-section>
                                      <div style="max-width: 400px;">
                                        <q-list dense>
                                          <q-item>
                                            <q-item-section> <div class="text-weight-medium">  {{  $q.lang.vuiOrchestra.orchestra.startTime }} </div> </q-item-section>
                                            <q-item-section side class="text-primary"> {{ formatDate(activity.beginTime) }} </q-item-section>
                                          </q-item>
                                          <q-item>
                                            <q-item-section> <div class="text-weight-medium">  {{  $q.lang.vuiOrchestra.orchestra.endTime }} </div> </q-item-section>
                                            <q-item-section side class="text-primary"> {{ formatDate(activity.endTime) }} </q-item-section>
                                          </q-item>
                                          <q-item>
                                            <q-item-section> <div class="text-weight-medium">  {{ $q.lang.vuiOrchestra.orchestra.duration }} </div> </q-item-section>
                                            <q-item-section side class="text-primary"> {{ activity.executionTime }}s </q-item-section>
                                          </q-item>
                                          <q-item v-if="activity.hasLogFile">
                                            <q-item-section> <div class="text-weight-medium">  {{ $q.lang.vuiOrchestra.orchestra.logFile }} </div> </q-item-section>
                                            <q-item-section side class="text-primary"> 
                                              <q-btn type="a" icon="description" flat padding="none" :title="$q.lang.vuiOrchestra.orchestra.logFile" :href="apiUrl+'/executions/'+execution.preId+'/activities/'+activity.aceId+'/attachment'"></q-btn>
                                            </q-item-section>
                                          </q-item>
                                          <q-item v-if="activity.hasTechnicalLog">
                                            <q-item-section> <div class="text-weight-medium">  {{ $q.lang.vuiOrchestra.orchestra.technicalLogFile }} </div> </q-item-section>
                                            <q-item-section side class="text-primary"> 
                                              <q-btn type="a" icon="description" flat padding="none" :title="$q.lang.vuiOrchestra.orchestra.technicalLogFile" :href="apiUrl+'/executions/'+execution.preId+'/activities/'+activity.aceId+'/logFile'"></q-btn>
                                            </q-item-section>
                                          </q-item>
                                        </q-list>
                                      </div>
                                    </q-card-section>
                                  </q-card> 
                                </q-expansion-item>
                                <q-separator></q-separator>
                              </div>
                            </q-list>
                          </q-tab-panel>
                          <q-tab-panel name="support" v-if="false">
                            <div class="text-h5"> {{ $q.lang.vuiOrchestra.orchestra.support }}
                              <q-btn  round color="primary"  icon="edit" class="q-ml-sm" size="sm" v-if="!orchestraUiReadOnly"></q-btn>
                            </div>
                            <q-separator class="q-mt-sm q-mb-md"></q-separator>
                            <div style="max-width: 400px;">
                              <q-list dense>
                                <q-item v-if="activity.hasLogFile">
                                  <q-item-section> <div class="text-weight-medium">  {{ $q.lang.vuiOrchestra.orchestra.logFile }} </div> </q-item-section>
                                  <q-item-section side class="text-primary"> 
                                    <q-btn type="a" icon="description" flat padding="none" :title="$q.lang.vuiOrchestra.orchestra.logFile" :href="apiUrl+'/executions/'+execution.preId+'/activities/'+activity.aceId+'/attachment'"></q-btn>
                                  </q-item-section>
                                </q-item>
                                <q-item>
                                  <q-item-section>  <div class="text-weight-medium">  {{ $q.lang.vuiOrchestra.orchestra.support }} </div> </q-item-section>
                                  <q-item-section side> To fill ! </q-item-section>
                                </q-item>
                                <q-item>
                                  <q-item-section> <div class="text-weight-medium"> {{ $q.lang.vuiOrchestra.orchestra.supportDate }} </div> </q-item-section>
                                  <q-item-section side> To fill ! </q-item-section>
                                </q-item>
                                <q-item>
                                  <q-item-section> <div class="text-weight-medium">  {{ $q.lang.vuiOrchestra.orchestra.comment }} </div> </q-item-section>
                                  <q-item-section side> To fill ! </q-item-section>
                                </q-item>
                              </q-list>
                            </div>
                          </q-tab-panel>
                        </q-tab-panels>
                      </template>
                    </q-splitter>
                  </q-card-section>
                </q-card>
              </q-expansion-item>
              <q-separator></q-separator>
            </div>
          </q-list>
          <template v-slot:loading>
            <div class="row justify-center q-my-md">
              <div v-if="!errorMessage && !executionsLoaded">
                <q-spinner-ios color="primary" size="2em"></q-spinner-ios>
              </div>
              <div v-else class="text-weight-medium">{{ errorMessage }}</div>
            </div>
          </template>
        </q-infinite-scroll>
      </div>
    </template>
  </q-splitter>
</template>

<script>
import Quasar from "quasar";
import axios from "axios";
export default {
  created() {
    axios
      .get(`${this.apiUrl}/definitions/${this.$props.processName}`)
      .then((res) => {
        this.processInfo = res.data;

        // Init technical form fields
        this.form.technical = {
          cronExpression: this.processInfo.triggeringStrategy  ? this.processInfo.triggeringStrategy.cronExpression : "",
          active: Object.entries(this.processInfo).length  ? this.processInfo.active  ? "true" : "false" : "-",
          multiExecution: this.processInfo.triggeringStrategy ? this.processInfo.triggeringStrategy.multiExecution ? "true" : "false" : "-",
          rescuePeriod: this.processInfo.triggeringStrategy  ? this.processInfo.triggeringStrategy.rescuePeriodInSeconds  : "",
        };

        // Init settings form fields

        this.form.settings = this.processInfo.triggeringStrategy
          ? this.processInfo.triggeringStrategy.initialParams
            ? { ...this.processInfo.triggeringStrategy.initialParams }
            : {}
          : {};
      })
      .catch((err) => {
        console.error(err);
        if (err.response) {
          this.errorMessage = `${this.$q.lang.vuiOrchestra.orchestra.noProcess}: ${this.$props.processName}`;
        } else {
          this.errorMessage = this.$q.lang.vuiOrchestra.orchestra.connectionFailed;
        }
        this.connectionFailure = err.response ? false : true;
      });
    axios
      .get(`${this.apiUrl}/executions/summaries/${this.$props.processName}`)
      .then((res) => {
        this.processSummary = res.data;
      })
      .catch((err) => {
        console.error(err);
      });
    //this.updateExecutions("");
  },
  props: ["apiUrl", "orchestraUiReadOnly", "processName"],
  data() {
    return {
      editMode: { technical: false, settings: false },
      executionsLoaded: false,
      limit: 20,
      offset: 0,
      connectionFailure: "?",
      errorMessage: "",
      status: "",
      processInfo: {},
      processSummary: {},
      filterTab: "all", // Tab for filtering executions
      executions: [],
      activities: {},
      expandedExecutions: {},
      form: {
        technical: {},
        settings: {},
      },
      tabs: {}, //Tabs for navigating inside execution
      splitterModel: 50,
      splitterModelExecutions: 20,
    };
  },
  watch: {
    "$q.lang": function() {
      // Avoid displaying message when this.connectionFailure == ?
      if (this.errorMessage) {
        if (this.connectionFailure == false) {
          this.errorMessage = `${this.$q.lang.vuiOrchestra.orchestra.noProcess}: ${this.$props.processName}`;
        } else if (this.connectionFailure == true) {
          this.errorMessage = this.$q.lang.vuiOrchestra.orchestra.connectionFailed;
        }
      }
    },
  },
  methods: {
    getIconFromExecutionState(executionStatus) {
      switch(executionStatus) {
        case 'DONE' :
          return 'done';
        case 'ERROR' :
          return 'error';
        case 'ABORTED' :
          return 'flash_on';
        default:
          return 'help'
      } 
    },
    getColorFromExecutionState(executionStatus) {
      switch(executionStatus) {
        case 'DONE' :
          return 'green';
        case 'ERROR' :
          return 'red';
        case 'ABORTED' :
          return 'orange';
        default:
          return 'grey'
      } 
    },
    formatDate(unformattedDate) {
      // Date is assumed to be in YYYY-MM-DDTHH:mm... format
      if(unformattedDate) {
        let timestamp = Quasar.date.extractDate(
          unformattedDate.substring(0, 16),
          "YYYY-MM-DDTHH:mm"
        );
        return Quasar.date.formatDate(timestamp, "DD/MM/YYYY HH:mm");
      }
      return "";
    },
    executeNow: function(myProcessName) {
      axios
        .post(`${this.apiUrl}/executionsControl/executeNow`, { processName: myProcessName })
        .then((res) => {
          this.$q.notify({
            message: this.$q.lang.vuiOrchestra.orchestra.executeNowOk,
            color: 'positive'
          });
        })
        .catch((err) => {
          console.error(err);
          this.$q.notify({
            message: this.$q.lang.vuiOrchestra.orchestra.executeNowErr,
            color: 'negative'
          });
        });
    },
    fetchActivities: function(preId) {
      axios
        .get(`${this.apiUrl}/executions/${preId}/activities`)
        .then((res) => {
          this.activities[preId] = res.data
        })
        .catch((err) => {
          console.error(err);
        });
    },
    updateExecutions: function(status) {
      this.limit = 20;
      this.offset = 0;
      this.status = status;
      this.errorMessage = "";
      this.connectionFailure = "?";
      axios
        .get(
          `${this.apiUrl}/executions/?processName=${this.$props.processName}&status=${this.status}&limit=${this.limit}`
        )
        .then((res) => {
          this.executions = res.data;
          res.data.map((execution) => {
            this.tabs[execution.preId] = "info";
          });
        })
        .catch((err) => {
          console.error(err);
          if (err.response) {
            this.errorMessage = `${this.$q.lang.vuiOrchestra.orchestra.noProcess}: ${this.$props.processName}`;
          } else {
            this.errorMessage = this.$q.lang.vuiOrchestra.orchestra.connectionFailed;
          }
          this.connectionFailure = err.response ? false : true;
        });
    },
    onLoad(index, done) {
      axios
        .get(
          `${this.apiUrl}/executions/?processName=${this.$props.processName}&status=${this.status}&limit=${this.limit}&offset=${this.offset}`
        )
        .then((res) => {
          this.executions.push(...res.data)
          res.data.map((execution) => {
            this.tabs[execution.preId] = "info";
          });
          if (res.data.length <  this.limit) {
            this.executionsLoaded = true;
            done(true);
          } 
          done(false)
        });
        this.offset += 20;
    },
    onSubmit(form) {
      if (form == "technical") {
        axios
          .put(
            `${this.apiUrl}/definitions/${this.$props.processName}/properties`,
            {
              cronExpression: this.form.technical.cronExpression,
              multiExecution: this.form.technical.multiExecution == "true",
              rescuePeriod: this.form.technical.rescuePeriod,
              active: this.form.technical.active == "true",
            }
          )
          .then(this.$router.go())
          .catch(console.error);
      } else if (form == "settings") {
        axios
          .put(`${this.apiUrl}/definitions/${this.$props.processName}/params`, {
            initialParams: this.form.settings,
          })
          .then(this.$router.go())
          .catch(console.error);
      }
    },
  },
};
</script>
