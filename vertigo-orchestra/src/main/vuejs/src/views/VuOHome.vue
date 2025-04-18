<template>
  <div class="row justify-center">
    <div style="width: 1300px;">
      <div class="text-h5 row q-mt-lg">
        <div class="q-mx-auto">
          {{ $q.lang.vuiOrchestra.orchestra.title.part1 }} {{ startOfWeek }}
          {{ $q.lang.vuiOrchestra.orchestra.title.part2 }} {{ endOfWeek }}
        </div>
      </div>
      <div class="row q-mt-lg">
        <q-tabs v-model="tab" inline-label class="text-primary q-mx-auto">
          <q-tab
            name="all"
            icon="list"
            :label="$q.lang.vuiOrchestra.orchestra.all"
            @click="getStatus('A')"
          ></q-tab>
          <!--Status must not be empty nor in lowercase letters. Any uppercase string would do the job as long as it is different than the keywords: SUCCESS, ERROR et MISFIRED -->
          <q-tab
            name="success"
            icon="done"
            :label="$q.lang.vuiOrchestra.orchestra.success"
            @click="getStatus('SUCCESS')"
            class="text-green"
          ></q-tab>
          <q-tab
            name="error"
            icon="error"
            :label="$q.lang.vuiOrchestra.orchestra.error"
            @click="getStatus('ERROR')"
            class="text-red"
          ></q-tab>
          <q-tab
            name="misfired"
            icon="timer_off"
            :label="$q.lang.vuiOrchestra.orchestra.misfired"
            @click="getStatus('MISFIRED')"
            class="text-grey"
          ></q-tab>
        </q-tabs>
      </div>
      <div class="row q-mt-lg">
        <div class="q-mx-auto q-gutter-md">
          <q-btn
            color="primary"
            icon="navigate_before"
            :label="$q.lang.vuiOrchestra.orchestra.previousWeek"
            @click="getWeek(-1)"
          ></q-btn>
          <q-btn round color="primary" icon="today" @click="getWeek(0)"></q-btn>
          <q-btn
            color="primary"
            icon-right="navigate_next"
            :label="$q.lang.vuiOrchestra.orchestra.nextWeek"
            @click="getWeek(1)"
          ></q-btn>
        </div>
      </div>
      <div class="q-pa-md">
        <q-table
          :rows="data"
          :columns="columns"
          row-key="name"
          :loading="loading"
        >
          <template #body="props">
            <q-tr
              :props="props"
              @click="$router.push('/process/' + props.row.processName)"
            >
              <q-td key="processLabel" :props="props">
                {{ props.row.processLabel }}
              </q-td>
              <q-td key="state" :props="props">
                <div class="row q-col-gutter-x-md">
                  <div class="col text-center">
                    <q-icon name="done" size="sm" class="text-green"></q-icon>
                  </div>
                  <div class="col text-center">
                    <q-icon name="error" size="sm" class="text-red"></q-icon>
                  </div>
                  <div class="col text-center">
                    <q-icon
                      name="timer_off"
                      size="sm"
                      class="text-grey"
                    ></q-icon>
                  </div>
                </div>
                <div class="row q-col-gutter-x-md">
                  <div class="col text-center">
                    {{ props.row.successfulCount }}
                  </div>
                  <div class="col text-center">{{ props.row.errorsCount }}</div>
                  <div class="col text-center">
                    {{ props.row.misfiredCount }}
                  </div>
                </div>
              </q-td>
              <q-td key="lastExecutionTime" :props="props">
                {{ props.row.lastExecutionTime }}
              </q-td>
              <q-td key="nextExecutionTime" :props="props">
                {{ props.row.nextExecutionTime }}
              </q-td>
            </q-tr>
          </template>
          <template v-slot:no-data>
            <div class="full-width row flex-center q-gutter-sm">
              <span v-if="loading">
                {{ $q.lang.vuiOrchestra.orchestra.loading }} <q-spinner />
              </span>
              <span v-else-if="fail">
                {{ $q.lang.vuiOrchestra.orchestra.connectionFailed }}
              </span>
              <span v-else>{{ $q.lang.vuiOrchestra.orchestra.noData }}</span>
            </div>
          </template>
        </q-table>
      </div>
    </div>
  </div>
</template>

<script>
import Quasar from "quasar";
import axios from "axios";
export default {
  created() {
    this.getWeek(0);
  },
  methods: {
    formatDate(unformattedDate) {
      // Date is assumed to be in YYYY-MM-DDTHH:mm... format
      let timestamp = Quasar.date.extractDate(
        unformattedDate.substring(0, 16),
        "YYYY-MM-DDTHH:mm"
      );
      return Quasar.date.formatDate(timestamp, "DD/MM/YYYY");
    },
    updateData() {
      this.loading = true;
      this.fail = false;
      this.data = [];
      axios
        .get(
          `${this.apiUrl}/executions/summaries?weekOffset=${this.offset}&status=${this.status}`
        )
        .then((res) => {
          res.data.map((process) => {
            process.lastExecutionTime = process.lastExecutionTime
              ? this.formatDate(process.lastExecutionTime)
              : "-";
            process.nextExecutionTime = process.nextExecutionTime
              ? this.formatDate(process.nextExecutionTime)
              : "-";
            this.data.push(process);
          });
          this.loading = false;
        })
        .catch((err) => {
          this.fail = true;
          this.loading = false;
          console.error(err);
        });
    },
    getWeekLimits: function(offset) {
      let dayInWeek = Quasar.date.addToDate(Date.now(), {
        days: offset * 7,
      });
      let dayOfWeek = Quasar.date.getDayOfWeek(Date.now());
      let startOfWeek = Quasar.date.subtractFromDate(dayInWeek, {
        days: dayOfWeek ? dayOfWeek - 1 : 6,
      });
      let endOfWeek = Quasar.date.addToDate(dayInWeek, {
        days: dayOfWeek ? 7 - dayOfWeek : 0,
      });
      return {
        startOfWeek: Quasar.date.formatDate(startOfWeek, "DD/MM/YYYY"),
        endOfWeek: Quasar.date.formatDate(endOfWeek, "DD/MM/YYYY"),
      };
    },
    getWeek(week) {
      this.offset = week ? this.offset + week : 0;
      let weekLimits = this.getWeekLimits(this.offset);
      this.startOfWeek = weekLimits.startOfWeek;
      this.endOfWeek = weekLimits.endOfWeek;
      if (!this.status) {
        this.status = "A";
      }
      this.updateData();
    },
    getStatus(status) {
      this.status = status;
      this.updateData();
    },
  },
  props: ["apiUrl"],
  data() {
    return {
      columns: [
        {
          name: "processLabel",
          label: this.$q.lang.vuiOrchestra.orchestra.processLabel,
          align: "left",
          field: "processLabel",
        },
        {
          name: "state",
          label: this.$q.lang.vuiOrchestra.orchestra.state,
          field: "state",
          align: "center",
        },
        {
          name: "lastExecutionTime",
          label: this.$q.lang.vuiOrchestra.orchestra.lastExecutionTime,
          field: "lastExecutionTime",
        },
        {
          name: "nextExecutionTime",
          label: this.$q.lang.vuiOrchestra.orchestra.nextExecutionTime,
          field: "nextExecutionTime",
        },
      ],
      data: [],
      loading: false,
      fail: false,
      offset: 0,
      status: "A", //Status must not be empty nor in lowercase letters. Any uppercase string would do the job as long as it is different than the keywords: SUCCESS, ERROR et MISFIRED
      tab: "all",
      startOfWeek: "",
      endOfWeek: "",
    };
  },
  watch: {
    "$q.lang": function() {
      this.columns = this.columns.map((column) => {
        return { ...column, label: this.$q.lang.vuiOrchestra.orchestra[column.name] };
      });
    },
  },
};
</script>

<style lang="scss">
tr:hover {
  cursor: pointer;
}
</style>
