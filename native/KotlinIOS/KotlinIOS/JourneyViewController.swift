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
    
    var journey: JourneyInfo? = nil
    var tickets: [TicketInfo] = []
    @IBOutlet weak var ticketTable: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        presenter.onViewTaken(view: self)
    }
}

extension JourneyViewController : JourneyInfoContractView {
    func setJourney(journey: JourneyInfo) {
        self.journey = journey
    }
    
    func setTickets(tickets: [TicketInfo]) {
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
        cell.ticketPrice.text = "£\(Float(ticket.price) / 100.0)"
        
        return cell
    }
}

