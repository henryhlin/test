/**
 *  Monitor Door Status
 *
 *  Copyright 2015 Henry Lin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Monitor Door Status",
    namespace: "henryhlin",
    author: "Henry Lin",
    description: "Monitors the status of a door and notifies if it is open.",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/garage_contact.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/garage_contact@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Meta/garage_contact@2x.png")


preferences {
	section("Door") {
		input "doorsensor", "capability.contactSensor", title: "Which Door Sensor?"
        //input "openthreshold", "number", title: "Notify if door is open longer than X minutes", description:  "Number of minutes"
    }
    section("Send Notifications to?"){
    	input("recipients", "contact", title: "Send notifications to") {
        	input "phone", "phone", title: "Notify phone with text message", description: "Phone Number 1"
            input "phone", "phone", title: "Notify secondary phone with text message (optional)", description: "Phone Number 2", required: false
        }
    }
}

def installed() {
	//log.trace "Installed with settings: ${settings}"
	runEvery5Minutes(doorOpenCheck)
    initialize()
}

def updated() {
	//log.trace "Updated with settings: ${settings}"
	unsubscribe()
    runEvery5Minutes(doorOpenCheck)
	initialize()
}

def initialize() {
	subscribe("doorsensor", "contact", doorcontact)
}

def doorOpenCheck()
{
	def currentstate = doorSensor.ContactState
    log.debug "doorOpenCheck"
    if (currentState?.value == "open") {
    	log.debug "${doorswitch.displayName} is open"
        sendNotification("${doorswitch.displayName} is open.", [method: "both", phone: "phone"])
        }
        
}