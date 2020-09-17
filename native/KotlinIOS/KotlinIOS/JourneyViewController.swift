//
//  JourneyViewController.swift
//  KotlinIOS
//
//  Created by Will Page on 14/09/2020.
//  Copyright © 2020 Evgeny Petrenko. All rights reserved.
//

import UIKit
import SharedCode

class JourneyViewController: UIViewController {
    
    private var presenter: JourneyInfoContractPresenter = JourneyInfoPresenter()
    
    var journey: Journey? = nil
    var tickets: [Ticket] = []
    @IBOutlet weak var ticketTable: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        presenter.onViewTaken(view: self)
    }
    
    @IBAction func onDone(_ sender: Any) {
        self.dismiss(animated: true, completion: {
            self.presentingViewController?.dismiss(animated: true, completion: nil)
        })
    }
}

extension JourneyViewController : JourneyInfoContractView {
    func setJourney(journey: Journey) {
        self.journey = journey
    }
    
    func setTickets(tickets: [Ticket]) {
        self.tickets = tickets
        
        ticketTable.reloadData()
    }
}

extension JourneyViewController : UITableViewDelegate, UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tickets.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellIdentifier = "TicketTableViewCell"
        guard let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as? TicketTableViewCell else {
            fatalError("The dequeued cell is not an instance of TicketTableViewCell")
        }
        let ticket = tickets[indexPath.row]
        
        cell.ticketName.text = ticket.name
        cell.ticketPrice.text = String(format: "£%.2f", Float(ticket.price) / 100)
        
        return cell
    }
}
