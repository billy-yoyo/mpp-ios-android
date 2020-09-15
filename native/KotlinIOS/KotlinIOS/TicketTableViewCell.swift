//
//  TicketTableViewCell.swift
//  KotlinIOS
//
//  Created by Will Page on 14/09/2020.
//  Copyright © 2020 Evgeny Petrenko. All rights reserved.
//

import UIKit

class TicketTableViewCell: UITableViewCell {

    @IBOutlet weak var ticketName: UILabel!
    @IBOutlet weak var ticketPrice: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
