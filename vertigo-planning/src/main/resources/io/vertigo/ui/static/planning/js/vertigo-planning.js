VUiExtensions.methods.fromCalendarTime = function(date) {
    //return date.day+'/'+date.month+'/'+date.year+' '+date.hour+':'+date.minute;
    //2022-01-23T22:22:39.124Z
    ;
    return date.date + 'T' + date.time + ':00.000Z';
}

//var PARSE_REGEX = /^(\d{1,2})\/(\d{1,2})\/(\d{4})?([^\d]+(\d{1,2}))?(:(\d{1,2}))?(:(\d{1,2}))?(.(\d{1,3}))?$/;
//var PARSE_REGEX = /^(\d{1,4})-(\d{1,2})-(\d{2})?([^\d]+(\d{1,2}))?(:(\d{1,2}))?(:(\d{1,2}))?(.(\d{1,3}))Z?$/;
var PARSE_REGEX_ISO = /^(\d{1,4})-(\d{1,2})-(\d{2})$/;
var PARSE_REGEX_FMT = /^(\d{1,2})\/(\d{1,2})\/(\d{4})$/;
VUiExtensions.methods.toCalendarDate = function(vDate, minutesOfDay) {
    if(!vDate) return null;
    if(vDate.includes('-')) {
        return this.toCalendarDateIso(vDate, minutesOfDay);
    }
    return this.toCalendarDateFmt(vDate, minutesOfDay);
}
VUiExtensions.methods.toCalendarDateIso = function(vDate, minutesOfDay) {
    
    // YYYY-MM-DD hh:mm
    var parts = PARSE_REGEX_ISO.exec(vDate);
    if (!parts) { return null }

    return {
        date: parts[1] + '-' + parts[2] + '-' + parts[3],
        time: Math.floor(minutesOfDay/60) + ':' + minutesOfDay%60,
        year: parseInt(parts[1], 10),
        month: parseInt(parts[2], 10),
        day: parseInt(parts[3], 10) || 1,
        hour: Math.floor(minutesOfDay/60) || 0,
        minute: minutesOfDay%60 || 0,
        weekday: 0,
        doy: 0,
        workweek: 0,
        hasDay: true,
        hasTime: !!(minutesOfDay),
        past: false,
        current: false,
        future: false,
        disabled: false
    }
}

VUiExtensions.methods.toCalendarDateFmt= function(vDate, minutesOfDay) {
    // DD/MM/YYYY hh:mm
    var parts = PARSE_REGEX_FMT.exec(vDate);
    if (!parts) { return null }

    return {
        date: parts[3] + '-' + parts[2] + '-' + parts[1],
        time: Math.floor(minutesOfDay/60) + ':' + minutesOfDay%60,
        year: parseInt(parts[3], 10),
        month: parseInt(parts[2], 10),
        day: parseInt(parts[1], 10) || 1,
        hour: Math.floor(minutesOfDay/60) || 0,
        minute: minutesOfDay%60 || 0,
        weekday: 0,
        doy: 0,
        workweek: 0,
        hasDay: true,
        hasTime: !!(minutesOfDay),
        past: false,
        current: false,
        future: false,
        disabled: false
    }
}

VUiExtensions.methods.isCssColor = function(color) {
    return !!color && !!color.match(/^(#|(rgb|hsl)a?\()/)
};

VUiExtensions.methods.plageStateColor = function(event) {
   return (event.nbNonPublie>0?{class:"trh-nonPublie", label:"Non publié", miniLabel:"NP"} 
    : event.nbPlanifie>0?{class:"trh-planifie", label:"Planifie", miniLabel:"Pl"}
    : event.nbPublie>0 ? {class:"trh-publie", label:"Publié", miniLabel:"Pu"} 
	: {class:"trh-none", label:"none", miniLabel:"No"});
};

VUiExtensions.methods.badgeClasses = function(event, type) {
    const cssColor = this.isCssColor(event.bgcolor);
    const plageState = this.plageStateColor(event);
    const isHeader = type === 'header'
    return {
        [`bg-${plageState.class}`]: !cssColor,
        'full-width': !isHeader && (!event.side || event.side === 'full'),
        'left-side': !isHeader && event.side === 'left',
        'right-side': !isHeader && event.side === 'right'
    }
};

VUiExtensions.methods.badgeStyles = function(event, type, timeStartPos, timeDurationHeight) {
    const s = {}
    if (this.isCssColor(event.bgcolor)) {
        s['background-color'] = event.bgcolor
        s.color = luminosity(event.bgcolor) > 0.5 ? 'black' : 'white'
    }
    if (timeStartPos) {
        s.top = timeStartPos(event.eventCalendar.time,false) + 'px'
    }
    if (timeDurationHeight) {
        let endMinuteOfDay = event.eventEndCalendar.hour*60+event.eventEndCalendar.minute;
        let startMinuteOfDay = event.eventCalendar.hour*60+event.eventCalendar.minute;
        s.height = (timeDurationHeight(endMinuteOfDay - startMinuteOfDay) - 3) + 'px'
    }
    //s['align-items'] = 'flex-start'
    return s
};

VUiExtensions.methods.intervalStart = function(allEvents) {
    var minTime = (7*60+30); //7h30
    for (let i = 0; i < allEvents.length; ++i) {
        minTime = Math.min(minTime, allEvents[i].minutesDebut);
    }
    return minTime/30; //30min per interval 
}

VUiExtensions.methods.intervalCount = function(allEvents) {
    var maxTime = (18*60+30); //18h30
    for (let i = 0; i < allEvents.length; ++i) {
        maxTime = Math.max(maxTime, allEvents[i].minutesFin);
    }
    var maxInterval = maxTime/30; //30min per interval 
    var minInterval = this.intervalStart(allEvents); 
    return maxInterval-minInterval; //18h30 - 7h30
}

VUiExtensions.methods.getEvents = function(dt, allEvents) {
    const currentDate = QCalendarDay.parsed(dt);
    const selectedEvents = [];

    allEvents.forEach(event => {
        let added = false;
        const eventCalendar = this.toCalendarDate(event.dateLocale, event.minutesDebut);
        const eventEndCalendar = this.toCalendarDate(event.dateLocale, event.minutesFin);
		// Création d'une copie propre de l'événement sans modifier l'objet original
        const eventCopy = { ...event, eventCalendar, eventEndCalendar };

	    if (eventCalendar.date === dt) {
            if (eventCalendar.time) {
				const start1 = QCalendarDay.parsed(eventCalendar.date + ' ' + eventCalendar.time);
				const end1 = QCalendarDay.parsed(eventEndCalendar.date + ' ' + eventEndCalendar.time);				                        
				const overlappingEvents = selectedEvents.filter(selectedEvent => {
                    if (selectedEvent.eventCalendar.time) {
                        const start2 = QCalendarDay.parsed(selectedEvent.eventCalendar.date + ' ' + selectedEvent.eventCalendar.time);
                        const end2 = QCalendarDay.parsed(selectedEvent.eventEndCalendar.date + ' ' + selectedEvent.eventEndCalendar.time);

                        return start1.date != end2.date && start2.date != end1.date && (
                            QCalendarDay.isBetweenDates(start1, start2, end2, true) ||
                            QCalendarDay.isBetweenDates(end1, start2, end2, true)
                        );
                    }
                    return false;
                });
										
				if (overlappingEvents.length > 0) {
											              
						// Fusionner les événements chevauchants
						const minEvent = [eventCopy, ...overlappingEvents].reduce((minEvt, evt) => 
							evt.minutesDebut < minEvt.minutesDebut ? evt : minEvt);
						const maxEvent = [eventCopy, ...overlappingEvents].reduce((maxEvt, evt) => 
							evt.minutesFin > maxEvt.minutesFin ? evt : maxEvt);
						
						// Agréger les valeurs numériques 
		                const nbNonPublieSum = overlappingEvents.reduce((sum, evt) => sum + evt.nbNonPublie, event.nbNonPublie);
		                const nbPlanifieSum = overlappingEvents.reduce((sum, evt) => sum + evt.nbPlanifie, event.nbPlanifie);
		                const nbPublieSum = overlappingEvents.reduce((sum, evt) => sum + evt.nbPublie, event.nbPublie);
		                const nbReserveSum = overlappingEvents.reduce((sum, evt) => sum + evt.nbReserve, event.nbReserve);
		                const nbReserveNonPublieSum = overlappingEvents.reduce((sum, evt) => sum + evt.nbReserveNonPublie, event.nbReserveNonPublie);
		                const nbTotalSum = overlappingEvents.reduce((sum, evt) => sum + evt.nbTotal, event.nbTotal);
						
						// Créer la liste des événements fusionnés (contenant tous les événements individuels)
						const eventList = [eventCopy, ...overlappingEvents].map(evt => evt.eventList || [evt]).flat();
						// Fusionner les listes d'événements sans doublons
	                    const uniqueEventList = eventList.filter((value, index, array) => array.indexOf(value) === index);
															
		                // Ajouter l'événement fusionné
		                selectedEvents.push({
		                    ...eventCopy,
							eventCalendar: minEvent.eventCalendar,
		                    eventEndCalendar: maxEvent.eventEndCalendar,
		                    minutesDebut: minEvent.minutesDebut,  // Conserver minutesDebut du premier
							minutesDebut_fmt: minEvent.minutesDebut_fmt,  // Conserver minutesDebut_fmt du plus petit
							minutesFin: maxEvent.minutesFin,  // Conserver minutesFin du dernier
							minutesFin_fmt: maxEvent.minutesFin_fmt,  // Conserver minutesFin_fmt du dernier
		                    nbNonPublie: nbNonPublieSum,
		                    nbPlanifie: nbPlanifieSum,
		                    nbPublie: nbPublieSum,
		                    nbReserve: nbReserveSum,
		                    nbReserveNonPublie: nbReserveNonPublieSum,
		                    nbTotal: nbTotalSum,
		                    ageId: minEvent.ageId,
		                    plhId: minEvent.plhId,
		                    eventList: uniqueEventList
		                });

		                // Retirer les événements individuels fusionnés
		                overlappingEvents.forEach(overlappingEvent => {
		                    const index = selectedEvents.indexOf(overlappingEvent);
		                    if (index !== -1) {
		                        selectedEvents.splice(index, 1);
		                    }
		                });
		            } else {
		                selectedEvents.push({ ...eventCopy, eventCalendar, eventEndCalendar });
		            }
				}
        } else if (eventCalendar.days) {
            const startDate = QCalendarDay.parsed(eventCalendar.date);
            const endDate = QCalendarDay.addToDate(startDate, { day: eventCalendar.days });
            if (QCalendarDay.isBetweenDates(currentDate, startDate, endDate)) {
                selectedEvents.push({ ...eventCopy, eventCalendar, eventEndCalendar });
            }
        }
    });

    return selectedEvents;
};

// Week-Agenda
VUiExtensions.methods.getNameAsTwoLetters = function (name) {
  if (!name || typeof name !== 'string') return ''; // Si le nom est invalide, retourner une chaîne vide
  // Nettoyage du nom : supprime les mots entre parenthèses, normalise les espaces autour des tirets,
    // et retire tout ce qui n'est pas une lettre ou un chiffre.
    name = name
      .replace(/\(.*\)/g, ' ') // Supprime les mots entre parenthèses
      .replace(/\s*-\s*/g, '-') // Normalise les espaces autour des tirets
      .replace(/[^a-zA-Z0-9\s-]+/g, '') // Retire tout ce qui n'est pas lettre, chiffre, espace ou tiret
      .trim(); // Supprime les espaces en début et fin de chaîne
  
  if (!name) return ''; // Si le nom est vide après nettoyage
  // Nettoyer et séparer le nom par le séparateur principal
  const parts = name.split(/\s+/);
  if (parts.length > 1) {
      // Si le nom est composé de plusieurs mots, on prend les initiales du premier et du dernier mot
      return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
  }
  if (name.includes('-')) { // Si le nom est unique, on vérifie s'il contient un tiret
	// Si le nom contient un tiret, séparer par les tirets
	const subParts = name.split('-');
    return (subParts[0][0] + subParts[subParts.length - 1][0]).toUpperCase();
  }
  // Si c'est un seul mot, prendre les deux premières lettres, la deuxième en minuscule
  return name.slice(0, 1).toUpperCase() + name.slice(1, 2).toLowerCase();   
}

VUiExtensions.methods.formatMinutes= function(minutes) {
            let min = '' + minutes % 60;
            let heure = '' + (minutes - min) / 60
            return heure.padStart(2, '0') + ':' + min.padStart(2, '0')
        }
VUiExtensions.methods.searchDefaultPlageHoraireForm = function(dateLocale, weekday, minutesOfDay) {            
            for (var defaultPlageIdx in this.$data.vueData.defaultPlageHoraire) {
               let defaultPlage = this.$data.vueData.defaultPlageHoraire[defaultPlageIdx];
               if(defaultPlage.jourDeSemaine === weekday 
                    && defaultPlage.minutesDebut <= minutesOfDay 
                    && defaultPlage.minutesFin >= minutesOfDay) {
                    return { 
                            'dateLocale' : dateLocale,
                            'minutesDebut' : this.formatMinutes(defaultPlage.minutesDebut),
                            'minutesFin' : this.formatMinutes(defaultPlage.minutesFin),
                            'nbGuichet' : defaultPlage.nbGuichet,
                            };
               }
            }
            return;
        }    
VUiExtensions.methods.onCreatePlageHoraireDefault = function() {
            var plageHoraireForm = this.$data.vueData.creationPlageHoraireForm;
            let dateTime = this.toCalendarDate(this.$data.vueData.agendaRange.firstDate,0);
            let day = '' + dateTime.day;
            let month = '' + dateTime.month;
            plageHoraireForm.dateLocale = day.padStart(2, '0')+'/'+month.padStart(2, '0')+'/'+dateTime.year
            
            var defaultPlage = this.$data.vueData.defaultPlageHoraire[0];
            plageHoraireForm.minutesDebut = this.formatMinutes(defaultPlage.minutesDebut);
            plageHoraireForm.minutesFin = this.formatMinutes(defaultPlage.minutesFin);
            plageHoraireForm.nbGuichet = defaultPlage.nbGuichet;
            this.$data.componentStates.createItemModal.opened = true;
        }
VUiExtensions.methods.onCreatePlageHoraireDateTime = function(data) {
                let dateTime = data.scope.timestamp;
                let day = '' + dateTime.day;
                let month = '' + dateTime.month;
                let minutesOfDay = dateTime.hour*60+dateTime.minute;
                
                var plageHoraireForm = this.$data.vueData.creationPlageHoraireForm;
                plageHoraireForm.dateLocale = day.padStart(2, '0')+'/'+month.padStart(2, '0')+'/'+dateTime.year
                if(plageHoraireForm.ageId < 0) {
                    plageHoraireForm.ageId = null;
                }
                let dureeCreneau = plageHoraireForm.dureeCreneau;
                dureeCreneau = dureeCreneau?dureeCreneau:5; //if not set default to 5
                var mod = minutesOfDay % dureeCreneau;
                minutesOfDay += mod < (dureeCreneau+1)/2 ? -mod : (dureeCreneau-mod);
                
                var defaultPlage = this.searchDefaultPlageHoraireForm(plageHoraireForm.dateLocale, dateTime.weekday, minutesOfDay);
                if(defaultPlage) {
                    plageHoraireForm.minutesDebut = defaultPlage.minutesDebut;
                    plageHoraireForm.minutesFin = defaultPlage.minutesFin;
                    plageHoraireForm.nbGuichet = defaultPlage.nbGuichet;
                } else {
                    plageHoraireForm.minutesDebut = this.formatMinutes(minutesOfDay)
                    plageHoraireForm.minutesFin = this.formatMinutes(minutesOfDay+240)
                    plageHoraireForm.nbGuichet = 1;            
                }
                this.$data.componentStates.createItemModal.opened = true;
        } 
VUiExtensions.methods.onPublishPlageHoraires = function() {
                this.$data.vueData.publicationTrancheHoraireForm.publicationMinutesDebut = this.$data.vueData.publicationTrancheHoraireForm.publicationMinutesDebut_fmt;
                this.$data.componentStates.publishItemModal.opened = true;
        }
VUiExtensions.methods.doPublishPlageHoraires = function() {
            let formFull = this.$data.vueData.publicationTrancheHoraireForm;
            let formData = {
                'vContext[publicationTrancheHoraireForm][publishNow]': this.$data.componentStates.publishItemModal.selectedTab==='publie',
                'vContext[publicationTrancheHoraireForm][dateLocaleDebut]': formFull.dateLocaleDebut,
                'vContext[publicationTrancheHoraireForm][dateLocaleFin]': formFull.dateLocaleFin,
                'vContext[publicationTrancheHoraireForm][publicationDateLocale]': formFull.publicationDateLocale,
                'vContext[publicationTrancheHoraireForm][publicationMinutesDebut]': formFull.publicationMinutesDebut,
            };
            this.httpPostAjax('_publishPlage', formData, {
                 onSuccess: function() {
                    this.$q.notify({message: 'Publication effectuée', type :'positive'});
                    this.$data.componentStates.publishItemModal.opened = false
                }.bind(this)
            });
        } 
VUiExtensions.methods.onDuplicateWeek= function() {
                this.httpPostAjax('_prepareDuplicateSemaine', {}, {
                 onSuccess: function() {
                    this.$data.componentStates.duplicateWeekModal.opened = true;
                }.bind(this)
            })
        }
VUiExtensions.methods.doDuplicateWeek = function() {
            let formData = this.vueDataParams(['duplicationSemaineForm']);
            this.httpPostAjax('_duplicateSemaine', formData, {
                 onSuccess: function() {
                    this.$q.notify({message: 'Duplication effectuée', type :'positive'});
                    this.$data.componentStates.duplicateWeekModal.opened = false
                }.bind(this)
            });
        } 
VUiExtensions.methods.createPlageHoraire = function(modeGuichet) {
            let formData = this.vueDataParams(['creationPlageHoraireForm']);
            if(modeGuichet) {
                formData.delete('vContext[creationPlageHoraireForm][nbGuichet]');
            }
            formData.delete('vContext[creationPlageHoraireForm][dureeCreneau]');
            this.httpPostAjax('_createPlage', formData, {
                 onSuccess: function() {
                    this.$q.notify({message: 'Plage horaire créée', type :'positive'});
                    this.$data.componentStates.createItemModal.opened = false
                }.bind(this)
            });
        }
VUiExtensions.methods.createPlageHoraireGuichet = function() {
            createPlageHoraire(true);
        }
VUiExtensions.methods.confirmDeletePlageHoraire = function(event) {
            this.$data.componentStates.editedPlageHoraire = event;
            this.$data.componentStates.confirmDeleteItemModal.opened = true;
        }
VUiExtensions.methods.deletePlageHoraire = function(plhId) {
            this.httpPostAjax('_deletePlage', {plhId: plhId}, {
                 onSuccess: function() {
                    this.$q.notify({message: 'Plage horaire supprimée', type :'positive'});
                    this.$data.componentStates.confirmDeleteItemModal.opened = false;
                }.bind(this)
            });
        }
VUiExtensions.methods.onSelectPlageHoraire = function(event) {
            this.$data.componentStates.editedPlageHoraire = event;
            this.httpPostAjax('_loadPlageHoraireDetail', {plhId: event.plhId}, {
                 onSuccess: function() {
                     this.$data.componentStates.trancheHorairesList.pagination.rowsNumber = this.$data.vueData.trancheHoraires.length;
                     this.$data.componentStates.viewItemDrawer.opened = true;
                }.bind(this)
            })
        }
        
window.addEventListener('vui-before-plugins', function(event) {
		event.detail.vuiAppInstance.component("QCalendarDay", QCalendarDay.QCalendarDay);
	}
)